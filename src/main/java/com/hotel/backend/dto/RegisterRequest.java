// dto/RegisterRequest.java
package com.hotel.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
}