package com.lostkingdom.demo.controller;

import com.alibaba.fastjson.JSON;
import com.lostkingdom.demo.entity.AuthUser;
import com.lostkingdom.demo.service.AuthUserMongoService;
import com.lostkingdom.demo.util.RedisUtil;
import com.lostkingdom.demo.vo.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

/**
 * @program: demo
 * @description: 测试用接口
 * @author: jiang.yin
 * @create: 2020-04-01 14:51
 **/
@RestController
@RequestMapping("/test")
@Api("测试类接口")
public class TestController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    AuthUserMongoService authUserMongoService;

    @RequestMapping(value = "/redis")
    @ApiOperation(value = "redis连接测试",httpMethod = "GET")
    public Message redisTest(){
        redisUtil.sSet("test","test");

        return Message.success();
    }

    @RequestMapping(value = "/mongoAddUser")
    @ApiOperation(value = "mongoDB插入操作",httpMethod = "POST")
    public Message mongoAddUserTest(AuthUser user){
        authUserMongoService.insert(user);
        return Message.success();
    }

    @RequestMapping(value = "/mongoQueryUser")
    @ApiOperation(value = "mongoDB查询操作",httpMethod = "POST")
    public Message mongoQueryUserTest(){
        Message msg = new Message();
        msg.setData(authUserMongoService.findAllUser());
        return msg;
    }
}
