package com.openclassroom.projet_spring_api_chatopcg.controller;

import com.openclassroom.projet_spring_api_chatopcg.configuration.JwtUtils;
import com.openclassroom.projet_spring_api_chatopcg.dto.RentalsDTO;
import com.openclassroom.projet_spring_api_chatopcg.entity.Rentals;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.repository.RentalsRepository;
import com.openclassroom.projet_spring_api_chatopcg.repository.UserRepository;
import com.openclassroom.projet_spring_api_chatopcg.response.MessageResponse;
import com.openclassroom.projet_spring_api_chatopcg.response.RentalsResponse;
import com.openclassroom.projet_spring_api_chatopcg.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RentalsController {

    private final FileStorageService fileStorageServiceRepository;
    private final JwtUtils jwtUtils;
    private final RentalsRepository rentalsRepository;
    private final UserRepository userRepository;

    @Operation(
            summary = "Récupérer toute les locations",
            description = "Permet de récuperer tout les locations de tous les utilisateurs"
    )
    @GetMapping("/rentals")
    public ResponseEntity<?> getAllRentalsInfos() {
        List<Rentals> listRentals = rentalsRepository.findAll();
        if (listRentals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rentals not found");
        }
        List<RentalsDTO> rentalsDTOS = listRentals.stream()
                .map(RentalsDTO::fromEntityGet)
                .toList();

        RentalsResponse response = new RentalsResponse(rentalsDTOS);

        return ResponseEntity.ok(response);

    }

    @Operation(
            summary = "Récupere une seule location",
            description = "Permet de récupérer une seule location, exemple : rentals/1"

    )
    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRentalsInfo(@PathVariable int id) {
        Rentals rentals = rentalsRepository.findById(id);
        if (rentals == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rentals not found");
        }
        RentalsDTO rentalsDTO = RentalsDTO.fromEntityGet(rentals);

        return ResponseEntity.ok(rentalsDTO);
    }

    @Operation(
            summary = "Ajoute une nouvelle location",
            description = "Permet de ajouter une location pour l'utilisateur du compte"
    )
    @PostMapping(value = "/rentals", consumes = "multipart/form-data")
    public ResponseEntity<?> addRental(
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") double price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("description") String description) {


        // Récupérez l'utilisateur correspondant à l'email
        User user = userRepository.findByEmail(jwtUtils.getAuthenticatedUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Rentals rental = new Rentals();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setCreated_at(LocalDateTime.now());
        rental.setUpdated_at(LocalDateTime.now());

        if (!picture.isEmpty()) {
            String savedImagePath = fileStorageServiceRepository.save(picture);
            String imageUrl = "http://localhost:3001/uploads/" + savedImagePath;
            rental.setPicture(imageUrl);
        }
        rental.setUser(user);

        rentalsRepository.save(rental);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Rental created !"));
    }

    @Operation(
            summary = "Modifier une location",
            description = "Modifie les locations de l'utilisateur principal"
    )
    @PutMapping(value = "/rentals/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> putRentalsInfo(
            @PathVariable int id,
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") double price,
            @RequestParam("description") String description) {

        User user = userRepository.findByEmail(jwtUtils.getAuthenticatedUsername());

        Rentals existingRental = rentalsRepository.findById(id);
        if (existingRental == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rentals not found");
        }
        if (existingRental.getUser().getId() == user.getId()) {
            if (existingRental.getName().equals(name)) {
                existingRental.setName(name);
            }
            if (existingRental.getSurface() > 0) {
                existingRental.setSurface(surface);
            }
            if (existingRental.getPrice() > 0) {
                existingRental.setPrice(price);
            }
            if (existingRental.getDescription() != null) {
                existingRental.setDescription(description);
            }
            existingRental.setUpdated_at(LocalDateTime.now());
            rentalsRepository.save(existingRental);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("You do not have the right to modify it");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Rental updated !"));

    }

}
