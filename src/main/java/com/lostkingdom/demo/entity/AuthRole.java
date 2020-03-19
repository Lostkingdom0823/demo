package com.lostkingdom.demo.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yin.jiang
 * @date 2019/8/13 16:49
 */
public class AuthRole implements Serializable {

    private Long id;
    private String description;
    private String name;
    private String status;
    private Date createTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
}
