// dto/RoomResponse.java
package com.hotel.backend.dto;

import com.hotel.backend.entity.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String numero;
    private String type;
    private int etage;
    private double prixParNuit;
    private RoomStatus statut;
}