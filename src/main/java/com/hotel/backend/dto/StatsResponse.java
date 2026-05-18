package com.hotel.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private long totalChambres;
    private long chambresDisponibles;
    private long chambresOccupees;
    private long totalReservations;
    private long reservationsEnAttente;
    private long reservationsConfirmees;
    private long totalClients;
    private double revenuTotal;
}