package com.lostkingdom.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordUtil {

    public static String passwordEncoder(String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    public String passwordDecoder(){
        return null;
    }

}
