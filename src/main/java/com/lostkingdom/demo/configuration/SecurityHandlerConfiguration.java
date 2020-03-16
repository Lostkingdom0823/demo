package com.lostkingdom.demo.configuration;

import cn.hutool.core.collection.CollectionUtil;
import com.lostkingdom.demo.enums.CommonExceptionEnums;
import com.lostkingdom.demo.exception.DemoException;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
            if (response.isCommitted()) {
                log.debug("Response has already been committed");
                return;
            }
            throw new DemoException(CommonExceptionEnums.NO_AUTHORITY);
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            ResponseUtil.responseJsonWriter(response, new Message(CommonExceptionEnums.NO_AUTHORITY));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, e) -> {
            if (response.isCommitted()) {
                log.debug("Response has already been committed");
                return;
            }
            throw new DemoException(CommonExceptionEnums.ACCESS_DENIED);
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            ResponseUtil.responseJsonWriter(response, new Message(CommonExceptionEnums.ACCESS_DENIED));
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

            ResponseUtil.responseJsonWriter(response, Message.success(map));
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            if (response.isCommitted()) {
                log.debug("Response has already been committed");
                return;
            }
            throw new DemoException(CommonExceptionEnums.AUTHORITY_FAILED);
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            ResponseUtil.responseJsonWriter(response, new Message(CommonExceptionEnums.AUTHORITY_FAILED));
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
