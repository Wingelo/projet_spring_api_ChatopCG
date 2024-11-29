package com.openclassroom.projet_spring_api_chatopcg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassroom.projet_spring_api_chatopcg.entity.Rentals;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalsDTO {
    private int id;
    private String name;
    private double surface;
    private double price;
    private String description;
    private String picture;

    @JsonProperty("owner_id")
    private int userId;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    public static RentalsDTO fromEntityGet(Rentals rentals) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new RentalsDTO(
                rentals.getId(),
                rentals.getName(),
                rentals.getSurface(),
                rentals.getPrice(),
                rentals.getDescription(),
                rentals.getPicture(),
                rentals.getUser().getId(),
                Optional.ofNullable(rentals.getCreated_at())
                        .map(date -> date.format(formatter))
                        .orElse(null),
                Optional.ofNullable(rentals.getUpdated_at())
                        .map(date -> date.format(formatter))
                        .orElse(null)
        );
    }
}
