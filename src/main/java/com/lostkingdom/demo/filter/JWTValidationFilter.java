package com.lostkingdom.demo.filter;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.lostkingdom.demo.jwt.JWTTokenGenerator;
import com.lostkingdom.demo.jwt.JWTTokenPair;
import com.lostkingdom.demo.jwt.JWTTokenStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {

    private AuthenticationEntryPoint authenticationEntryPoint;

    private static final String AUTHENTICATION_PREFIX = "Bearer ";
    private JWTTokenGenerator jwtTokenGenerator;
    private JWTTokenStorage jwtTokenStorage;

    public JWTValidationFilter(JWTTokenGenerator jwtTokenGenerator, JWTTokenStorage jwtTokenStorage, AuthenticationEntryPoint authenticationEntryPoint){
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtTokenStorage = jwtTokenStorage;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 如果已经通过认证
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(AUTHENTICATION_PREFIX)) {
            String jwtToken = header.replace(AUTHENTICATION_PREFIX, "");


            if (StringUtils.hasText(jwtToken)) {
                try {
                    authenticationTokenHandle(jwtToken, request);
                } catch (AuthenticationException e) {
                    authenticationEntryPoint.commence(request, response, e);
                }
            } else {
                // 带安全头 没有带token
                authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException("token is not found"));
            }

        }
        filterChain.doFilter(request, response);
    }

    private void authenticationTokenHandle(String jwtToken, HttpServletRequest request) throws AuthenticationException {

        // 根据我的实现 有效token才会被解析出来
        JSONObject jsonObject = jwtTokenGenerator.decodeAndVerify(jwtToken);

        if (Objects.nonNull(jsonObject)) {
            String username = jsonObject.getStr("aud");

            // 从缓存获取 token
            JWTTokenPair jwtTokenPair = jwtTokenStorage.get(username);
            if (Objects.isNull(jwtTokenPair)) {
                if (log.isDebugEnabled()) {
                    log.debug("token : {}  is  not in cache", jwtToken);
                }
                // 缓存中不存在就算 失败了
                throw new CredentialsExpiredException("token is not in cache");
            }
            String accessToken = jwtTokenPair.getAccessToken();

            if (jwtToken.equals(accessToken)) {
                // 解析 权限集合  这里
                JSONArray jsonArray = jsonObject.getJSONArray("roles");

                String roles = jsonArray.toString();

                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
                User user = new User(username, "[PROTECTED]", authorities);
                // 构建用户认证token
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 放入安全上下文中
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                // token 不匹配
                if (log.isDebugEnabled()){
                    log.debug("token : {}  is  not in matched", jwtToken);
                }

                throw new BadCredentialsException("token is not matched");
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("token : {}  is  invalid", jwtToken);
            }
            throw new BadCredentialsException("token is invalid");
        }
    }
}
