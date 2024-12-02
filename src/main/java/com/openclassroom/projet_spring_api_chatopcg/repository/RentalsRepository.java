package com.openclassroom.projet_spring_api_chatopcg.repository;

import com.openclassroom.projet_spring_api_chatopcg.entity.Rentals;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalsRepository extends JpaRepository<Rentals, Integer> {

    Rentals findById(int id);

    List<Rentals> findByUser(User user);

}
