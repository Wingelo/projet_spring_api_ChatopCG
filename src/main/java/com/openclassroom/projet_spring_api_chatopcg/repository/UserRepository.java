package com.openclassroom.projet_spring_api_chatopcg.repository;

import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findById(int id);
}
