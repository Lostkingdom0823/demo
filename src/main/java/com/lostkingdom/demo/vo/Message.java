package com.lostkingdom.demo.vo;

import com.lostkingdom.demo.enums.CommonExceptionEnums;
import lombok.Data;
import java.util.Date;

@Data
public class Message {

    private Date currentTimestamp;
    private String code;
    private String msg;
    private Object data;

    public Message(){

    }

    public Message(CommonExceptionEnums commonExceptionEnums){
        this.currentTimestamp = new Date();
        this.code = commonExceptionEnums.getCode();
        this.msg = commonExceptionEnums.getMsg();
    }

}
