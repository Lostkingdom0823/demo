package com.lostkingdom.demo.entity;

import lombok.Data;

import java.util.Date;


@Data
public class BaseEntity {

    private Date createTimestamp;
    private Long createBy;
    private String createUsername;
    private Date updateTimestamp;
    private Long updateBy;
    private String updateUsername;
}
