package com.example.jwtAuthentication.repository;

import com.example.jwtAuthentication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface UserRepo extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);
}
