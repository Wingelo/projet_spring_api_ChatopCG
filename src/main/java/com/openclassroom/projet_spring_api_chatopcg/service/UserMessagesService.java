package com.openclassroom.projet_spring_api_chatopcg.service;

import com.openclassroom.projet_spring_api_chatopcg.entity.Rentals;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.entity.UserMessages;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserMessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMessagesService {
    private final UserMessagesRepository userMessagesRepository;

    public List<UserMessages> findByUser(User user) {
        return userMessagesRepository.findByUser(user);
    }
}
