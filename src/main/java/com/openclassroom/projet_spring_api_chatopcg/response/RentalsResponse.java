package com.openclassroom.projet_spring_api_chatopcg.response;

import com.openclassroom.projet_spring_api_chatopcg.dto.RentalsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalsResponse {
    private List<RentalsDTO> rentals;
}
