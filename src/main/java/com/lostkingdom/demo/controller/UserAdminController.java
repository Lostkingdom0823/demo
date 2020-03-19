package com.lostkingdom.demo.controller;

import com.lostkingdom.demo.service.IUserAdminService;
import com.lostkingdom.demo.vo.Message;
import com.lostkingdom.demo.vo.reqVo.UserAdminReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: demo
 * @description: 用户管理接口
 * @author: jiang.yin
 * @create: 2020-03-17 09:56
 **/
@RestController
@RequestMapping("/user")
public class UserAdminController {

    @Autowired
    IUserAdminService IUserAdminService;

    @RequestMapping("register")
    public Message addUser(@RequestBody UserAdminReqVo vo){
        if(IUserAdminService.addUser(vo)>0){
            return Message.success();
        }
        else {
            return new Message("500","用户创建失败");
        }
    }

    @RequestMapping("update")
    public Message updateUser(@RequestBody UserAdminReqVo vo){
        if(IUserAdminService.updateUser(vo)>0){
            return Message.success();
        }
        else {
            return new Message("500","用户更改失败");
        }
    }

    @RequestMapping("delete")
    public Message deleteUser(@RequestBody UserAdminReqVo vo){
        if(IUserAdminService.deleteUser(vo)>0){
            return Message.success();
        }
        else {
            return new Message("500","用户删除失败");
        }
    }

    @RequestMapping("query")
    public Message queryUser(@RequestBody UserAdminReqVo vo){
        return Message.success(IUserAdminService.queryUser(vo));
    }

    @RequestMapping("queryDetail")
    public Message queryUserDetail(@RequestBody UserAdminReqVo vo){
        return null;
    }

    @RequestMapping("changePassword")
    public Message changePassword(@RequestBody UserAdminReqVo vo){
        return Message.success(IUserAdminService.changePassword(vo));
    }

}
