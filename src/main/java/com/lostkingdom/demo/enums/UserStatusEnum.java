package com.lostkingdom.demo.enums;

/**
 * @program: demo
 * @description: 用户状态枚举类
 * @author: jiang.yin
 * @create: 2020-03-17 14:17
 **/
public enum UserStatusEnum {

    ENABLE("ENABLE","启用",1),
    DISABLE("DISABLE","禁用",2);

    private String value;
    private String des;
    private Integer index;

    UserStatusEnum(String value, String des, Integer index){
        this.value = value;
        this.des = des;
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public String getDes() {
        return des;
    }

    public Integer getIndex() {
        return index;
    }
}
