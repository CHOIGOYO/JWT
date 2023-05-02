package com.choigoyo.repository;

import com.choigoyo.entity.UserEntityJWT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntityJWT,Long> {
    UserEntityJWT findByUserName(String userName);
}
