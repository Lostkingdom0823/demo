package com.lostkingdom.demo.controller;

import com.lostkingdom.demo.entity.AuthUser;
import com.lostkingdom.demo.mapper.AuthUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author yin.jiang
 * @date 2019/10/18 14:36
 */
public class BaseController {

    @Autowired
    AuthUserMapper authUserMapper;

    public AuthUser getUser() { //为了session从获取用户信息,可以配置如下
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        AuthUser authUser = authUserMapper.selectUser(username);
        authUser.setPassword("******");
        return authUser;
    }

    public User getSecurityUser(){
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal;
    }


}
