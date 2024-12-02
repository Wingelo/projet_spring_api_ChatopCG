package com.openclassroom.projet_spring_api_chatopcg.controller;


import com.openclassroom.projet_spring_api_chatopcg.configuration.JwtUtils;
import com.openclassroom.projet_spring_api_chatopcg.dto.RentalsDTO;
import com.openclassroom.projet_spring_api_chatopcg.dto.UserMessagesDTO;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.repository.RentalsRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserMessagesRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final RentalsRepository rentalsRepository;
    private final UserMessagesRepository userMessagesRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;


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

        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email Already Exists");
        }
        user.setRole(user.getRole() != null ? user.getRole() : "ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());

        userRepository.save(user);

        Map<String, Object> authData = new HashMap<>();
        authData.put("token", jwtUtils.generateToken(user.getEmail()));
        authData.put("type", "Bearer");

        return ResponseEntity.ok(authData);
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
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", jwtUtils.generateToken(user.getEmail()));
                authData.put("type", "Bearer");
                return ResponseEntity.ok(authData);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @Operation(
            summary = "Récuperer les informations de l'utilisateur connecté"
    )
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo() {

        String email = jwtUtils.getAuthenticatedUsername();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());


        List<RentalsDTO> rentals = rentalsRepository.findByUser(user)
                .stream()
                .map(RentalsDTO::fromEntityGet)
                .toList();
        response.put("rentals", rentals);

        List<UserMessagesDTO> messages = userMessagesRepository
                .findByUser(user)
                .stream()
                .map(UserMessagesDTO::fromEntity)
                .toList();
        response.put("messages", messages);


        return ResponseEntity.ok(response);
    }
}


