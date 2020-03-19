package com.lostkingdom.demo.vo.reqVo;

import lombok.Data;

import java.util.Date;

/**
 * @program: demo
 * @description: 视图基类
 * @author: jiang.yin
 * @create: 2020-03-17 10:00
 **/
@Data
public class BaseReqVo {

    private String token;
    private Integer page;
    private Integer pageSize;
    private Date startTime;
    private Date endTime;

}
