package com.hotel.backend.controller;

import com.hotel.backend.dto.ReservationRequest;
import com.hotel.backend.dto.ReservationResponse;
import com.hotel.backend.entity.ReservationStatus;
import com.hotel.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // Client crée une réservation
    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody ReservationRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(reservationService.create(request, email));
    }

    // Client voit ses réservations
    @GetMapping("/mes-reservations")
    public ResponseEntity<List<ReservationResponse>> myReservations(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(reservationService.getMyReservations(email));
    }

    // Client annule sa réservation
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        reservationService.cancel(id, email);
        return ResponseEntity.noContent().build();
    }

    // Admin/Réceptionniste — toutes les réservations
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONNISTE')")
    public ResponseEntity<List<ReservationResponse>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    // Admin/Réceptionniste — changer statut
    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONNISTE')")
    public ResponseEntity<ReservationResponse> updateStatut(
            @PathVariable Long id,
            @RequestParam ReservationStatus statut) {
        return ResponseEntity.ok(reservationService.updateStatut(id, statut));
    }
}