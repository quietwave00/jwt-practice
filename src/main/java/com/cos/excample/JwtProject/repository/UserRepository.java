package com.cos.excample.JwtProject.repository;

import com.cos.excample.JwtProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);

}
