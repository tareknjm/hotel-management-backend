package com.hotel.backend.service;

import com.hotel.backend.dto.StatsResponse;
import com.hotel.backend.entity.ReservationStatus;
import com.hotel.backend.entity.Role;
import com.hotel.backend.entity.RoomStatus;
import com.hotel.backend.repository.ReservationRepository;
import com.hotel.backend.repository.RoomRepository;
import com.hotel.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public StatsResponse getStats() {
        long totalChambres     = roomRepository.count();
        long disponibles       = roomRepository.findByStatut(RoomStatus.DISPONIBLE).size();
        long occupees          = roomRepository.findByStatut(RoomStatus.OCCUPEE).size();
        long totalReservations = reservationRepository.count();
        long enAttente         = reservationRepository.findByStatut(ReservationStatus.EN_ATTENTE).size();
        long confirmees        = reservationRepository.findByStatut(ReservationStatus.CONFIRMEE).size();
        long totalClients      = userRepository.findAll()
                .stream().filter(u -> u.getRole() == Role.CLIENT).count();

        double revenu = reservationRepository.findByStatut(ReservationStatus.CONFIRMEE)
                .stream()
                .mapToDouble(r -> {
                    long nuits = java.time.temporal.ChronoUnit.DAYS
                            .between(r.getDateDebut(), r.getDateFin());
                    return nuits * r.getRoom().getPrixParNuit();
                }).sum();

        return new StatsResponse(
                totalChambres, disponibles, occupees,
                totalReservations, enAttente, confirmees,
                totalClients, revenu
        );
    }
}