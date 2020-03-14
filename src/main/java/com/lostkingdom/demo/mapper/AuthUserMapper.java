package com.lostkingdom.demo.mapper;

import com.lostkingdom.demo.entity.AuthUser;

import java.util.List;

/**
 * @author kingj
 * @date 2019/8/13 15:09
 */
public interface AuthUserMapper {

    List<AuthUser> getAllAuthUser();

    AuthUser getAuthUserById(Long id);

    AuthUser getAuthUserByUsername(String username);

    AuthUser selectUser(String username);

}
