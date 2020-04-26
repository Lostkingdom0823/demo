package com.lostkingdom.demo.service.impl;

import com.lostkingdom.demo.entity.AuthUser;
import com.lostkingdom.demo.repository.AuthUserMongoRepository;
import com.lostkingdom.demo.service.AuthUserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: demo
 * @description:
 * @author: jiang.yin
 * @create: 2020-04-26 11:19
 **/
@Service
public class AuthUserMongoServiceImpl implements AuthUserMongoService {

    @Autowired
    AuthUserMongoRepository authUserMongoRepository;

    //查询所有
    @Override
    public List<AuthUser> findAllUser() {
        return authUserMongoRepository.findAll();
    }

    //添加
    @Override
    public void insert(AuthUser user) {
        authUserMongoRepository.insert(user);
    }

    //删除
    @Override
    public void delete(Long id) {
        authUserMongoRepository.deleteById(id);
    }

    //修改
    @Override
    public void update(AuthUser user) {
        authUserMongoRepository.save(user);
    }

}
