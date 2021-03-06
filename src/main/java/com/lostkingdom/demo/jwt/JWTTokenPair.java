package com.lostkingdom.demo.jwt;

import lombok.Data;

/**
 * @ClassName JWTTokenPair
 * @Author yin.jiang
 * @Date 2020/1/14 17:06
 * @Version 1.0
 */
@Data
public class JWTTokenPair {

    private static final long serialVersionUID = -8518897818107784049L;
    private String accessToken;
    private String refreshToken;

}
