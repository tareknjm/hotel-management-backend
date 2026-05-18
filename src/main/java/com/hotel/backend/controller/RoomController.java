package com.hotel.backend.controller;

import com.hotel.backend.dto.RoomRequest;
import com.hotel.backend.dto.RoomResponse;
import com.hotel.backend.entity.RoomStatus;
import com.hotel.backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // Public — tous les connectés peuvent voir les chambres
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAll() {
        return ResponseEntity.ok(roomService.getAll());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<RoomResponse>> getDisponibles() {
        return ResponseEntity.ok(roomService.getDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    // Admin seulement
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> create(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id,
                                               @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.update(id, request));
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONNISTE')")
    public ResponseEntity<RoomResponse> updateStatut(@PathVariable Long id,
                                                     @RequestParam RoomStatus statut) {
        return ResponseEntity.ok(roomService.updateStatut(id, statut));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}