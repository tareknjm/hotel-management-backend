package com.hotel.backend.repository;

import com.hotel.backend.entity.Reservation;
import com.hotel.backend.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByStatut(ReservationStatus statut);

    // Vérifie chevauchement de dates (RG01)
    @Query("""
        SELECT COUNT(r) > 0 FROM Reservation r
        WHERE r.room.id = :roomId
        AND r.statut != 'ANNULEE'
        AND r.dateDebut < :dateFin
        AND r.dateFin > :dateDebut
    """)
    boolean existsOverlap(
            @Param("roomId") Long roomId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}