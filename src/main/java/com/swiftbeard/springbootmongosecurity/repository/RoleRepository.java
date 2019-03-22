package com.swiftbeard.springbootmongosecurity.repository;

import com.swiftbeard.springbootmongosecurity.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}
