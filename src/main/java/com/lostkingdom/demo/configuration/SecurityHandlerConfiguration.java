package com.lostkingdom.demo.configuration;

import cn.hutool.core.collection.CollectionUtil;
import com.lostkingdom.demo.entity.RestBody;
import com.lostkingdom.demo.enums.CommonExceptionEnums;
import com.lostkingdom.demo.filter.JWTValidationFilter;
import com.lostkingdom.demo.filter.PreLoginFilter;
import com.lostkingdom.demo.jwt.JWTTokenGenerator;
import com.lostkingdom.demo.jwt.JWTTokenPair;
import com.lostkingdom.demo.jwt.JWTTokenStorage;
import com.lostkingdom.demo.util.ResponseUtil;
import com.lostkingdom.demo.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Configuration
public class SecurityHandlerConfiguration {

    private static final String LOGIN_PROCESSING_URL = "/login";

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            Message message = new Message(CommonExceptionEnums.AUTHORITY_FAILED);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter printWriter = response.getWriter();
            printWriter.print(message);
            printWriter.flush();
            printWriter.close();
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, e) -> {
            Message message = new Message(CommonExceptionEnums.ACCESS_DENIED);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter printWriter = response.getWriter();
            printWriter.print(message);
            printWriter.flush();
            printWriter.close();
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(JWTTokenGenerator jwtTokenGenerator) {
        return (request, response, authentication) -> {
            if (response.isCommitted()) {
                log.debug("Response has already been committed");
                return;
            }
            Map<String, Object> map = new HashMap<>(5);
            map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("flag", "success_login");
            User principal = (User) authentication.getPrincipal();

            String username = principal.getUsername();
            Collection<GrantedAuthority> authorities = principal.getAuthorities();
            Set<String> roles = new HashSet<>();
            if (CollectionUtil.isNotEmpty(authorities)) {
                for (GrantedAuthority authority : authorities) {
                    String roleName = authority.getAuthority();
                    roles.add(roleName);
                }
            }

            JWTTokenPair jwtTokenPair = jwtTokenGenerator.jwtTokenPair(username, roles, null);

            map.put("access_token", jwtTokenPair.getAccessToken());
            map.put("refresh_token", jwtTokenPair.getRefreshToken());
            response.setHeader("Authorization", jwtTokenPair.getAccessToken());

            ResponseUtil.responseJsonWriter(response, RestBody.okData(map, "登录成功"));
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            if (response.isCommitted()) {
                log.debug("Response has already been committed");
                return;
            }
            Map<String, Object> map = new HashMap<>(2);

            map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("flag", "failure_login");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseUtil.responseJsonWriter(response, RestBody.build(HttpStatus.UNAUTHORIZED.value(), map, "认证失败","-9999"));
        };
    }

    @Bean
    public JWTValidationFilter jwtValidationFilter(JWTTokenGenerator jwtTokenGenerator, JWTTokenStorage jwtTokenStorage,
                                                   AuthenticationEntryPoint authenticationEntryPoint){
        return new JWTValidationFilter(jwtTokenGenerator,jwtTokenStorage,authenticationEntryPoint);
    }

    @Bean
    public PreLoginFilter preLoginFilter() {
        return new PreLoginFilter(LOGIN_PROCESSING_URL, null);
    }


}
