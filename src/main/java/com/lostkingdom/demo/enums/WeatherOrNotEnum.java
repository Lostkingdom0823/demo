package com.lostkingdom.demo.enums;

/**
 * @program: demo
 * @description: 是否二项枚举类
 * @author: jiang.yin
 * @create: 2020-03-17 14:18
 **/
public enum WeatherOrNotEnum {

    NO(false,0,"NO"),
    YES(true,1,"YES");

    private Boolean value;
    private Integer index;
    private String des;

    WeatherOrNotEnum(Boolean value, Integer index, String des){
        this.value = value;
        this.index = index;
        this.des = des;
    }

    public Boolean getValue() {
        return value;
    }

    public Integer getIndex() {
        return index;
    }

    public String getDes() {
        return des;
    }
}
