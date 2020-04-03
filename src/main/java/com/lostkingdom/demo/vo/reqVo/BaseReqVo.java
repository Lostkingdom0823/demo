package com.lostkingdom.demo.vo.reqVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: demo
 * @description: 视图基类
 * @author: jiang.yin
 * @create: 2020-03-17 10:00
 **/
@Data
@ApiModel
public class BaseReqVo {

    @ApiModelProperty("登录凭证")
    private String token;

    @ApiModelProperty("分页页数")
    private Integer page;

    @ApiModelProperty("页面条数")
    private Integer pageSize;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

}
