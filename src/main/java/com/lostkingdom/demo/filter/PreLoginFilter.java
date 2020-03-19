package com.lostkingdom.demo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lostkingdom.demo.enums.LoginTypeEnum;
import com.lostkingdom.demo.util.ParameterRequestWrapper;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

public class PreLoginFilter extends GenericFilterBean {

    private static final String LOGIN_TYPE_KEY = "login_type";

    private RequestMatcher requiresAuthenticationRequestMatcher;
    private Map<LoginTypeEnum, LoginPostProcessor> processors = new HashMap<>();

    public PreLoginFilter(String loginProcessingUrl, Collection<LoginPostProcessor> loginPostProcessors) {
        Assert.notNull(loginProcessingUrl, "loginProcessingUrl must not be null");
        requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(loginProcessingUrl, "POST");
        LoginPostProcessor loginPostProcessor = defaultLoginPostProcessor();
        processors.put(loginPostProcessor.getLoginTypeEnum(), loginPostProcessor);

        if (!CollectionUtils.isEmpty(loginPostProcessors)) {
            loginPostProcessors.forEach(element -> processors.put(element.getLoginTypeEnum(), element));
        }

    }

    private LoginTypeEnum getTypeFromReq(ServletRequest request) {
        String parameter = request.getParameter(LOGIN_TYPE_KEY);

        int i = Integer.parseInt(parameter);
        LoginTypeEnum[] values = LoginTypeEnum.values();
        return values[i];
    }

    /**
     * 默认还是Form .
     *
     * @return the login post processor
     */
    private LoginPostProcessor defaultLoginPostProcessor() {
        return new LoginPostProcessor() {


            @Override
            public LoginTypeEnum getLoginTypeEnum() {

                return LoginTypeEnum.FORM;
            }

            @Override
            public String obtainUsername(ServletRequest request) {
                return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
            }

            @Override
            public String obtainPassword(ServletRequest request) {
                return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
            }
        };
    }

    private JSONObject translateInputStream(HttpServletRequest request){
        try {
            String resStr = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line;
            while((line=br.readLine())!=null){
                sb.append(line);
            }
            br.close();
            resStr=sb.toString();
            JSONObject json = JSONObject.parseObject(resStr);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper((HttpServletRequest) request);
        if (requiresAuthenticationRequestMatcher.matches((HttpServletRequest) request)) {

            if(((HttpServletRequest) request).getHeader("Content-Type").toLowerCase().contains("json")){
                JSONObject jsonObject = translateInputStream((HttpServletRequest) request);
                Map maps = (Map)JSON.parse(jsonObject.toJSONString());
                parameterRequestWrapper.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, String.valueOf(maps.get("username")));
                parameterRequestWrapper.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, String.valueOf(maps.get("password")));
            } else {
                LoginTypeEnum typeFromReq = getTypeFromReq(request);

                LoginPostProcessor loginPostProcessor = processors.get(typeFromReq);


                String username = loginPostProcessor.obtainUsername(request);

                String password = loginPostProcessor.obtainPassword(request);


                parameterRequestWrapper.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username);
                parameterRequestWrapper.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);
            }
        }

        chain.doFilter(parameterRequestWrapper, response);

    }
}
