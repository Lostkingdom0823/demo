package com.lostkingdom.demo.exception;


import com.lostkingdom.demo.enums.CommonExceptionEnums;
import lombok.Data;

@Data
public class DemoException extends RuntimeException {

    private CommonExceptionEnums commonExceptionEnums;

    public DemoException(CommonExceptionEnums commonExceptionEnums){
        this.commonExceptionEnums = commonExceptionEnums;
    }

}
