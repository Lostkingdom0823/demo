package com.lostkingdom.demo.dto;

import lombok.Data;

import java.util.Date;

/**
 * @program: demo
 * @description: 用户管理用户信息展示类
 * @author: jiang.yin
 * @create: 2020-03-17 10:23
 **/
@Data
public class UserAdminDTO {

    private Long id;
    private String username;
    private String name;
    private String status;
    private String phone;
    private Date createTimestamp;
    private Long createBy;
    private String createUsername;
    private Date updateTimestamp;
    private Long updateBy;
    private String updateUsername;

}
