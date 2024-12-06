package com.openclassroom.projet_spring_api_chatopcg.service;

import com.openclassroom.projet_spring_api_chatopcg.dto.RentalsDTO;
import com.openclassroom.projet_spring_api_chatopcg.entity.Rentals;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.repository.RentalsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalsService {
    private final RentalsRepository rentalsRepository;
    private final FileStorageService fileStorageServiceRepository;

    public Rentals findRentalById(int id) {
        return rentalsRepository.findById(id);
    }

    public List<Rentals> findByUser(User user) {
        return rentalsRepository.findByUser(user);
    }

    public Rentals addRental(String name, double surface, double price, String description, MultipartFile picture, User user) {
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
        return rental;
    }

    public Rentals updateRental(int id, String name, double surface, double price, String description, User user) {
        Rentals existingRental = rentalsRepository.findById(id);
        if (existingRental == null) {
            throw new RuntimeException("Rental not found");
        }


        if (existingRental.getUser().getId() != user.getId()) {
            throw new RuntimeException("You do not have the right to modify this rental");
        }
        if (name != null) {
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
        return existingRental;

    }

    public List<RentalsDTO> findRentalsByUser(User user) {
        return rentalsRepository.findByUser(user)
                .stream()
                .map(RentalsDTO::fromEntityGet)
                .collect(Collectors.toList());
    }
}