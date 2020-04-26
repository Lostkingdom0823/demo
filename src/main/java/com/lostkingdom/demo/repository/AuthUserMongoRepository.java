package com.lostkingdom.demo.repository;

import com.lostkingdom.demo.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;


@Component
public interface AuthUserMongoRepository extends MongoRepository<AuthUser, Long> {



}
