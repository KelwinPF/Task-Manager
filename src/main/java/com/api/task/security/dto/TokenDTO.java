package com.api.task.security.dto;
import lombok.Data;

@Data
public class TokenDTO {

    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}