package com.openclassroom.projet_spring_api_chatopcg.controller;

import com.openclassroom.projet_spring_api_chatopcg.configuration.JwtUtils;
import com.openclassroom.projet_spring_api_chatopcg.dto.UserMessagesDTO;
import com.openclassroom.projet_spring_api_chatopcg.entity.Rentals;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.entity.UserMessages;
import com.openclassroom.projet_spring_api_chatopcg.repository.RentalsRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserMessagesRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserRepository;
import com.openclassroom.projet_spring_api_chatopcg.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserMessagesController {

    private final JwtUtils jwtUtils;
    private final UserMessagesRepository userMessagesRepository;
    private final RentalsRepository rentalsRepository;
    private final UserRepository userRepository;

    @Operation(
            summary = "Permet de faire un message sur la location concerné",
            description = "Pour faire un message, il faut message, user_id,rental_id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Détails de l'utilisateur à enregistrer",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = "{ \"message\": \"Coucou\", \"user_id\": 1, \"rental_id\": 1 }"
                            )
                    )
            )
    )
    @PostMapping(value = "/messages")
    public ResponseEntity<?> addMessages(
            @RequestBody UserMessagesDTO userMessagesDTO
    ) {
        User user = userRepository.findByEmail(jwtUtils.getAuthenticatedUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("We could not find your profile, please try again");
        }

        Rentals rental = rentalsRepository.findById(userMessagesDTO.getRentalId());
        if (rental == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The rental does not exist");
        }

        UserMessages userMessages = new UserMessages();
        userMessages.setUser(user);
        userMessages.setRental(rental);
        userMessages.setMessage(userMessagesDTO.getMessage());
        userMessagesRepository.save(userMessages);


        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Message send with success"));
    }
}
