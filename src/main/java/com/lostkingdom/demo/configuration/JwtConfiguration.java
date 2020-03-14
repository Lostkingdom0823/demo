package com.lostkingdom.demo.configuration;

import com.lostkingdom.demo.jwt.JWTProperties;
import com.lostkingdom.demo.jwt.JWTTokenCacheStorage;
import com.lostkingdom.demo.jwt.JWTTokenGenerator;
import com.lostkingdom.demo.jwt.JWTTokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(JWTProperties.class)
@ConditionalOnProperty(prefix = "jwt.config",name = "enabled")
@Configuration
public class JwtConfiguration {

    @Bean
    public JWTTokenStorage jwtTokenStorage() {
        return new JWTTokenCacheStorage();
    }


    /**
     * Jwt token generator.
     *
     * @param jwtTokenStorage the jwt token storage
     * @param JWTProperties   the jwt properties
     * @return the jwt token generator
     */
    @Bean
    public JWTTokenGenerator jwtTokenGenerator(JWTTokenStorage jwtTokenStorage,JWTProperties JWTProperties) {
        return new JWTTokenGenerator(jwtTokenStorage, JWTProperties);
    }


}
