package com.lostkingdom.demo.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.lostkingdom.demo.jwt.JWTProperties.JWT_PREFIX;


/**
 * @ClassName JWTProperties
 * @Author yin.jiang
 * @Date 2020/1/14 16:22
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix=JWT_PREFIX)
@Configuration
public class JWTProperties {
    static final String JWT_PREFIX= "jwt.config";
    /**
     * 是否可用
     */
    private boolean enabled;
    /**
     * jks 路径
     */
    private String keyLocation;
    /**
     * key alias
     */
    private String keyAlias;
    /**
     * key store pass
     */
    private String keyPass;
    /**
     * jwt签发者
     **/
    private String iss;
    /**
     * jwt所面向的用户
     **/
    private String sub;
    /**
     * access jwt token 有效天数
     */
    private int accessExpDays;
    /**
     * refresh jwt token 有效天数
     */
    private int refreshExpDays;
}
