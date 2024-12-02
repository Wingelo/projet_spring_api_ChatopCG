package com.openclassroom.projet_spring_api_chatopcg.service;

import com.openclassroom.projet_spring_api_chatopcg.entity.Rentals;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import com.openclassroom.projet_spring_api_chatopcg.repository.RentalsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalsService {
    private final RentalsRepository rentalsRepository;

    public Rentals findRentalById(int id) {
        return rentalsRepository.findById(id);
    }

    public List<Rentals> findByUser(User user) {
        return rentalsRepository.findByUser(user);
    }
}