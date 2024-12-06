package com.openclassroom.projet_spring_api_chatopcg.controller;


import com.openclassroom.projet_spring_api_chatopcg.configuration.JwtUtils;
import com.openclassroom.projet_spring_api_chatopcg.dto.RentalsDTO;
import com.openclassroom.projet_spring_api_chatopcg.dto.UserMessagesDTO;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.service.AuthService;
import com.openclassroom.projet_spring_api_chatopcg.service.RentalsService;
import com.openclassroom.projet_spring_api_chatopcg.service.UserMessagesService;
import com.openclassroom.projet_spring_api_chatopcg.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final RentalsService rentalsService;
    private final UserMessagesService userMessagesService;
    private final AuthService authService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Operation(
            summary = "Enregistrer un nouvel utilisateur",
            description = "Permet à un utilisateur de s'enregistrer avec son email, son nom, et son mot de passe.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Détails de l'utilisateur à enregistrer",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = "{ \"email\": \"test@test5.com\", \"name\": \"test TEST\", \"password\": \"test!31\" }"
                            )
                    )
            )
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            Map<String, Object> authData = authService.register(user);
            return ResponseEntity.ok(authData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Permet à un utilisateur de se connecter en utilisant son email et son mot de passe.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Informations d'authentification de l'utilisateur",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = "{ \"email\": \"test@test5.com\", \"password\": \"test!31\" }"
                            )
                    )
            )
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Map<String, Object> authData = authService.login(user.getEmail(), user.getPassword());
            return ResponseEntity.ok(authData);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Récuperer les informations de l'utilisateur connecté"
    )
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo() {

        String email = jwtUtils.getAuthenticatedUsername();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        List<RentalsDTO> rentals = rentalsService.findRentalsByUser(user);
        List<UserMessagesDTO> messages = userMessagesService.getMessagesByUser(user);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("rentals", rentals);
        response.put("messages", messages);

        return ResponseEntity.ok(response);
    }
}


