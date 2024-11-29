package com.openclassroom.projet_spring_api_chatopcg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassroom.projet_spring_api_chatopcg.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private int id;
    private String name;
    private String email;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    public static UserDetailsDTO fromEntity(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new UserDetailsDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                Optional.ofNullable(user.getCreated_at())
                        .map(date -> date.format(formatter))
                        .orElse(null),
                Optional.ofNullable(user.getUpdated_at())
                        .map(date -> date.format(formatter))
                        .orElse(null)
        );
    }

}
