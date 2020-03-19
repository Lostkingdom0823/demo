package com.lostkingdom.demo.vo.reqVo;

import lombok.Data;

import java.util.Date;

/**
 * @program: demo
 * @description: 用户管理参数接收视图类
 * @author: jiang.yin
 * @create: 2020-03-17 10:01
 **/
@Data
public class UserAdminReqVo extends BaseReqVo {
    private Long id;
    private String username;
    private String name;
    private String password;
    private String status;
    private String phone;

}
