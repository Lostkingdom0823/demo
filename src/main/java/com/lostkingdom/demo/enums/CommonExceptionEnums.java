package com.lostkingdom.demo.enums;

public enum CommonExceptionEnums {

    AUTHORITY_FAILED("401","用户登录失败"),
    NO_AUTHORITY("401","请登录"),
    ACCESS_DENIED("403","用户无访问权限"),

    //用户管理错误
    NO_USERNAME("1001","用户名不能为空"),
    NO_PASSWORD("1002","密码不能为空"),
    NO_TRUENAME("1003","用户姓名不能为空"),
    NO_CREATOR_INFO("1004","创建人信息为空"),
    NO_USER_ID("1005","用户主键不能为空"),
    NO_USER_INFO("1006","用户信息为空"),
    NEW_PASSWORD_IS_NULL("1008","新密码不能为空"),
    OLD_PASSWORD_IS_NULL("1009","旧密码不能为空"),
    OLD_PASSWORD_IS_NOT_RIGHT("1010","旧密码不正确"),


    ;

    CommonExceptionEnums(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
