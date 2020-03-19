package com.lostkingdom.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author yin.jiang
 * @date 2019/8/13 11:34
 */
public class AuthUser extends BaseEntity implements Serializable {

    @Excel(name = "id",orderNum = "0")
    private Long id;
    @Excel(name = "用户名",orderNum = "1")
    private String username;
    @Excel(name = "密码",orderNum = "2")
    private String password;
    @Excel(name = "姓名",orderNum = "3")
    private String name;
    @Excel(name = "状态",orderNum = "4",replace = { "正常_ENABLE", "禁用_DISABLE" })
    private String status;

    private String phone;
    //@Excel(name = "用户角色",orderNum = "6")
    private List<AuthRole> userRoles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AuthRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<AuthRole> userRoles) {
        this.userRoles = userRoles;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
