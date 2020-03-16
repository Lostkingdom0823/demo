package com.lostkingdom.demo.enums;

public enum CommonExceptionEnums {

    AUTHORITY_FAILED("401","用户登录失败"),
    NO_AUTHORITY("401","请登录"),
    ACCESS_DENIED("403","用户无访问权限");

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
