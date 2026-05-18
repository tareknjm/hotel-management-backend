package com.hotel.backend.service;

import com.hotel.backend.dto.ReservationRequest;
import com.hotel.backend.dto.ReservationResponse;
import com.hotel.backend.entity.*;
import com.hotel.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationResponse create(ReservationRequest request, String email) {
        // Validation dates
        if (!request.getDateFin().isAfter(request.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        // Vérif chevauchement (RG01)
        boolean overlap = reservationRepository.existsOverlap(
                request.getRoomId(),
                request.getDateDebut(),
                request.getDateFin()
        );
        if (overlap) {
            throw new RuntimeException("Cette chambre est déjà réservée pour ces dates");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Chambre introuvable"));

        if (room.getStatut() != RoomStatus.DISPONIBLE) {
            throw new RuntimeException("Cette chambre n'est pas disponible");
        }

        Reservation reservation = Reservation.builder()
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .statut(ReservationStatus.EN_ATTENTE)
                .user(user)
                .room(room)
                .build();

        return toResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReservationResponse> getMyReservations(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return reservationRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ReservationResponse updateStatut(Long id, ReservationStatus statut) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));
        reservation.setStatut(statut);
        return toResponse(reservationRepository.save(reservation));
    }

    public void cancel(Long id, String email) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        // Un client ne peut annuler que ses propres réservations
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Action non autorisée");
        }

        if (reservation.getStatut() == ReservationStatus.CONFIRMEE) {
            throw new RuntimeException("Une réservation confirmée ne peut pas être annulée");
        }

        reservation.setStatut(ReservationStatus.ANNULEE);
        reservationRepository.save(reservation);
    }

    private ReservationResponse toResponse(Reservation r) {
        long nuits = ChronoUnit.DAYS.between(r.getDateDebut(), r.getDateFin());
        return new ReservationResponse(
                r.getId(),
                r.getDateDebut(),
                r.getDateFin(),
                r.getStatut(),
                r.getCreatedAt(),
                r.getUser().getId(),
                r.getUser().getNom(),
                r.getUser().getPrenom(),
                r.getRoom().getId(),
                r.getRoom().getNumero(),
                r.getRoom().getType(),
                r.getRoom().getPrixParNuit(),
                nuits,
                nuits * r.getRoom().getPrixParNuit()
        );
    }
}