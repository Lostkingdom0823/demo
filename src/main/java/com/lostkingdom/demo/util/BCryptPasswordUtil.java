package com.lostkingdom.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordUtil {

    public static String passwordEncoder(String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    public static Boolean passwordCheck(String password,String cipherText){
        BCryptPasswordEncoder bp = new BCryptPasswordEncoder();
        return bp.matches(password,cipherText);
    }

    public String passwordDecoder(){
        return null;
    }

}
