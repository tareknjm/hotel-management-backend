// dto/AuthRequest.java
package com.hotel.backend.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}