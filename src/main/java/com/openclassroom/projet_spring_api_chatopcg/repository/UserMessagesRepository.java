package com.openclassroom.projet_spring_api_chatopcg.repository;

import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.entity.UserMessages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMessagesRepository  extends JpaRepository<UserMessages, Integer> {

    List<UserMessages> findByUser(User user);
}
