package com.swiftbeard.springbootmongosecurity.repository;

import com.swiftbeard.springbootmongosecurity.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
}
