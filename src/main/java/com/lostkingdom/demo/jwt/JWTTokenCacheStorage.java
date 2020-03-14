package com.lostkingdom.demo.jwt;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @ClassName JWTTokenCacheStorage
 * @Author yin.jiang
 * @Date 2020/1/14 17:13
 * @Version 1.0
 */
public class JWTTokenCacheStorage implements JWTTokenStorage {

    private static final String TOKEN_CACHE = "usrTkn";


    @CachePut(value = TOKEN_CACHE, key = "#userId")
    @Override
    public JWTTokenPair put(JWTTokenPair jwtTokenPair, String userId) {
        return jwtTokenPair;
    }

    @CacheEvict(value = TOKEN_CACHE, key = "#userId")
    @Override
    public void expire(String userId) {
//        EhcacheHelper.remove(TOKEN_CACHE, uid);
    }


    @Cacheable(value = TOKEN_CACHE, key = "#userId")
    @Override
    public JWTTokenPair get(String userId) {
        return null;
    }

}
