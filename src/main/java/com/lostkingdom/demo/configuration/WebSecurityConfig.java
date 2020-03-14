package com.lostkingdom.demo.configuration;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lostkingdom.demo.entity.Rest;
import com.lostkingdom.demo.entity.RestBody;
import com.lostkingdom.demo.enums.CommonExceptionEnums;
import com.lostkingdom.demo.filter.JWTValidationFilter;
import com.lostkingdom.demo.filter.LoginPostProcessor;
import com.lostkingdom.demo.filter.PreLoginFilter;
import com.lostkingdom.demo.jwt.JWTTokenGenerator;
import com.lostkingdom.demo.jwt.JWTTokenPair;
import com.lostkingdom.demo.jwt.JWTTokenStorage;
import com.lostkingdom.demo.service.CustomUserService;
import com.lostkingdom.demo.util.ResponseUtil;
import com.lostkingdom.demo.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author yin.jiang
 * @date 2019/8/13 13:57
 */
@Component
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    CustomUserService customUserService;

    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    @Autowired
    JWTValidationFilter jwtValidationFilter;

    private static final String LOGIN_PROCESSING_URL = "/process";

    /**
     *
     * void configure(AuthenticationManagerBuilder auth) 用来配置认证管理器AuthenticationManager。
     * 说白了就是所有 UserDetails 相关的它都管，包含 PasswordEncoder 密码机
     * @date 2019/12/9
     * @author yin.jiang
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService).passwordEncoder(new BCryptPasswordEncoder()); //user Details Service验证
    }

    /**
     * void configure(HttpSecurity http) 这个是我们使用最多的，用来配置 HttpSecurity 。
     * HttpSecurity 用于构建一个安全过滤器链 SecurityFilterChain 。SecurityFilterChain 最终被注入核心过滤器 。
     * HttpSecurity 有许多我们需要的配置。我们可以通过它来进行自定义安全访问策略。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/test").permitAll()
                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
//                //.successForwardUrl("/")
//                // 第二个参数，如果不写成true，则默认登录成功以后，访问之前被拦截的页面，而非去我们规定的页面
//                //.failureForwardUrl("/loginFailure")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .logout()
                .addLogoutHandler(new CustomLogoutHandler())
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .and()
                .headers().frameOptions().disable()
                .and()
                .logout().permitAll(); //注销行为任意访问

        //http.addFilterAt();
    }

    /**
     * void configure(WebSecurity web) 用来配置 WebSecurity 。而 WebSecurity 是基于 Servlet Filter 用来配置 springSecurityFilterChain 。
     * 而 springSecurityFilterChain 又被委托给了 Spring Security 核心过滤器 Bean DelegatingFilterProxy 。
     * 相关逻辑你可以在 WebSecurityConfiguration 中找到。我们一般不会过多来自定义 WebSecurity ,
     * 使用较多的使其ignoring() 方法用来忽略 Spring Security 对静态资源的控制。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web){
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/**/*.js",
                "/**/*.css",
                "/**/*.jpg",
                "/**/*.png");
    }

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
    public JWTValidationFilter jwtValidationFilter(JWTTokenGenerator jwtTokenGenerator, JWTTokenStorage jwtTokenStorage,
                                                   AuthenticationEntryPoint authenticationEntryPoint){
        return new JWTValidationFilter(jwtTokenGenerator,jwtTokenStorage,authenticationEntryPoint);
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
    public PreLoginFilter preLoginFilter() {
        return new PreLoginFilter(LOGIN_PROCESSING_URL, null);
    }

    class CustomLogoutHandler implements LogoutHandler {
        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            String username = user.getUsername();
            log.info("username: {}  is offline now", username);
        }
    }

    class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            User user = (User) authentication.getPrincipal();
            String username = user.getUsername();
            log.info("username: {}  is offline now", username);


            responseJsonWriter(response, RestBody.ok("退出成功"));
        }

        private void responseJsonWriter(HttpServletResponse response, Rest rest) throws IOException {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ObjectMapper objectMapper = new ObjectMapper();
            String resBody = objectMapper.writeValueAsString(rest);
            PrintWriter printWriter = response.getWriter();
            printWriter.print(resBody);
            printWriter.flush();
            printWriter.close();
        }
    }

}
