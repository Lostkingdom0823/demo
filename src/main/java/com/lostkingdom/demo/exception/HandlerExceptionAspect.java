package com.lostkingdom.demo.exception;


import com.lostkingdom.demo.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class HandlerExceptionAspect {

    /** 
    * @Description:  自定义异常处理
    * @Param:  
    * @return:  
    * @Author: jiang.yin
    * @Date: 2020/3/16 
    */
    @ExceptionHandler(DemoException.class)
    @ResponseBody
    public Message exceptionProcessor(DemoException ex, HttpServletRequest request, HttpServletResponse response){
        Message exMessage = new Message();
        exMessage.setCode(ex.getCommonExceptionEnums().getCode());
        exMessage.setMsg(ex.getCommonExceptionEnums().getMsg());
        exMessage.setCurrentTimestamp(new Date());
        return exMessage;
    }

}
