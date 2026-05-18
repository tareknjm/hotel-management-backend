// dto/ReservationRequest.java
package com.hotel.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationRequest {
    private Long roomId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}