package com.openclassroom.projet_spring_api_chatopcg.service;

import com.openclassroom.projet_spring_api_chatopcg.dto.UserMessagesDTO;
import com.openclassroom.projet_spring_api_chatopcg.entity.Rentals;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.entity.UserMessages;
import com.openclassroom.projet_spring_api_chatopcg.repository.RentalsRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserMessagesRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserRepository;
import com.openclassroom.projet_spring_api_chatopcg.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMessagesService {
    private final UserMessagesRepository userMessagesRepository;
    private final UserRepository userRepository;
    private final RentalsRepository rentalsRepository;


    public List<UserMessages> findByUser(User user) {
        return userMessagesRepository.findByUser(user);
    }

    public List<UserMessagesDTO> getMessagesByUser(User user) {
        return userMessagesRepository.findByUser(user)
                .stream()
                .map(UserMessagesDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MessageResponse addMessage(UserMessagesDTO userMessagesDTO, String authenticatedEmail) {
        User user = userRepository.findByEmail(authenticatedEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "We could not find your profile, please try again");
        }

        Rentals rental = rentalsRepository.findById(userMessagesDTO.getRentalId());
        if (rental == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The rental does not exist");
        }

        UserMessages userMessages = new UserMessages();
        userMessages.setUser(user);
        userMessages.setRental(rental);
        userMessages.setMessage(userMessagesDTO.getMessage());

        userMessagesRepository.save(userMessages);

        return new MessageResponse("Message sent with success");
    }
}
