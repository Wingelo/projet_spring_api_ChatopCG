package com.openclassroom.projet_spring_api_chatopcg.controller;

import com.openclassroom.projet_spring_api_chatopcg.configuration.JwtUtils;
import com.openclassroom.projet_spring_api_chatopcg.dto.UserMessagesDTO;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.repository.RentalsRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserMessagesRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserRepository;
import com.openclassroom.projet_spring_api_chatopcg.response.MessageResponse;
import com.openclassroom.projet_spring_api_chatopcg.service.UserMessagesService;
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
    private final UserMessagesService userMessagesService;

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
        String authenticatedEmail = jwtUtils.getAuthenticatedUsername();
        MessageResponse response = userMessagesService.addMessage(userMessagesDTO, authenticatedEmail);
        return ResponseEntity.status(201).body(response);
    }
}
