package com.openclassroom.projet_spring_api_chatopcg.controller;

import com.openclassroom.projet_spring_api_chatopcg.configuration.JwtUtils;
import com.openclassroom.projet_spring_api_chatopcg.dto.UserDetailsDTO;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserMessagesRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserMessagesRepository userMessagesRepository;


    @Operation(
            summary = "Récuperer un utilisateur",
            description = "Permet de récupérer un seul utilisateur, exemple : user/1"

    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getOtherUserInfo(@PathVariable int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        UserDetailsDTO userDetailsDTO = UserDetailsDTO.fromEntity(user);

        return ResponseEntity.ok(userDetailsDTO);
    }


}
