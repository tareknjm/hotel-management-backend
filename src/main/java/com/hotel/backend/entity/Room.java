package com.hotel.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    private String type;
    private int etage;
    private double prixParNuit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus statut = RoomStatus.DISPONIBLE;
}