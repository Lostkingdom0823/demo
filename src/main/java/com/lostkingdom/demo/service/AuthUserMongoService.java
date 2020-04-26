package com.lostkingdom.demo.service;

import com.lostkingdom.demo.entity.AuthUser;

import java.util.List;

public interface AuthUserMongoService  {

    List<AuthUser> findAllUser();

    void insert(AuthUser user);

    void delete(Long id);

    void update(AuthUser user);

}
