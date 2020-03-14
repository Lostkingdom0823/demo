package com.lostkingdom.demo.jwt;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class JWTPayLoadBuilder {

    private Map<String, String> payload = new HashMap<>();
    /**
     * 附加的属性
     */
    private Map<String, String> additional;
    /**
     * jwt签发者
     **/
    private String iss;
    /**
     * jwt所面向的用户
     **/
    private String sub;
    /**
     * 接收jwt的一方
     **/
    private String aud;
    /**
     * jwt的过期时间，这个过期时间必须要大于签发时间
     **/
    private LocalDateTime exp;
    /**
     * jwt的签发时间
     **/
    private LocalDateTime iat = LocalDateTime.now();
    /**
     * 权限集
     */
    private Set<String> roles = new HashSet<>();
    /**
     * jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     **/
    private String jti = IdUtil.simpleUUID();

    public JWTPayLoadBuilder iss(String iss) {
        this.iss = iss;
        return this;
    }


    public JWTPayLoadBuilder sub(String sub) {
        this.sub = sub;
        return this;
    }

    public JWTPayLoadBuilder aud(String aud) {
        this.aud = aud;
        return this;
    }


    public JWTPayLoadBuilder roles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public JWTPayLoadBuilder expDays(int days) {
        Assert.isTrue(days > 0, "jwt expireDate must after now");
        this.exp = this.iat.plusDays(days);
        return this;
    }

    public JWTPayLoadBuilder additional(Map<String, String> additional) {
        this.additional = additional;
        return this;
    }

    public String builder() {
        payload.put("iss", this.iss);
        payload.put("sub", this.sub);
        payload.put("aud", this.aud);
        payload.put("exp", this.exp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        payload.put("iat", this.iat.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        payload.put("jti", this.jti);

        if (!CollectionUtils.isEmpty(additional)) {
            payload.putAll(additional);
        }
        payload.put("roles", JSON.toJSONString(this.roles));
        return JSON.toJSONString(payload);

    }

}
