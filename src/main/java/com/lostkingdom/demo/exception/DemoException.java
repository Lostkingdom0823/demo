package com.lostkingdom.demo.exception;


import com.lostkingdom.demo.enums.CommonExceptionEnums;
import lombok.Data;

@Data
public class DemoException extends Exception {

    private CommonExceptionEnums commonExceptionEnums;

    DemoException(CommonExceptionEnums commonExceptionEnums){
        this.commonExceptionEnums = commonExceptionEnums;
    }

}
