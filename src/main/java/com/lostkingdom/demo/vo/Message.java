package com.lostkingdom.demo.vo;

import com.lostkingdom.demo.enums.CommonExceptionEnums;
import lombok.Data;
import java.util.Date;

@Data
public class Message<T> {

    private Date currentTimestamp;
    private String code;
    private String msg;
    private T data;

    public Message(){

    }

    public Message(CommonExceptionEnums commonExceptionEnums){
        this.currentTimestamp = new Date();
        this.code = commonExceptionEnums.getCode();
        this.msg = commonExceptionEnums.getMsg();
    }

    public static Message success(){
        Message message = new Message();
        message.setCurrentTimestamp(new Date());
        message.setCode("200");
        message.setMsg("success");
        return message;
    }

    public static Message success(Object data){
        Message message = new Message();
        message.setCurrentTimestamp(new Date());
        message.setCode("200");
        message.setMsg("success");
        message.setData(data);
        return message;
    }

}
