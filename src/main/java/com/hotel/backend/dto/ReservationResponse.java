// dto/ReservationResponse.java
package com.hotel.backend.dto;

import com.hotel.backend.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private ReservationStatus statut;
    private LocalDateTime createdAt;
    private Long userId;
    private String userNom;
    private String userPrenom;
    private Long roomId;
    private String roomNumero;
    private String roomType;
    private double prixParNuit;
    private long nombreNuits;
    private double prixTotal;
}