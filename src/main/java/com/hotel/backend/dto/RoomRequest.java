// dto/RoomRequest.java
package com.hotel.backend.dto;

import lombok.Data;

@Data
public class RoomRequest {
    private String numero;
    private String type;
    private int etage;
    private double prixParNuit;
}