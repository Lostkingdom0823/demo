package com.lostkingdom.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class BaseEntity implements Serializable {

    private Date createTimestamp;
    private Long createBy;
    private String createUsername;
    private Date updateTimestamp;
    private Long updateBy;
    private String updateUsername;
}
