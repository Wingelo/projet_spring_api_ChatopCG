package com.openclassroom.projet_spring_api_chatopcg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassroom.projet_spring_api_chatopcg.entity.UserMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessagesDTO {

    private int id;
    private String message;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("rental_id")
    private int rentalId;

    public static UserMessagesDTO fromEntity(UserMessages userMessage) {
        return new UserMessagesDTO(
                userMessage.getId(),
                userMessage.getMessage(),
                userMessage.getUser().getId(),
                userMessage.getRental().getId()
        );
    }
}