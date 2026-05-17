package com.hotel.backend.repository;

import com.hotel.backend.entity.Room;
import com.hotel.backend.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatut(RoomStatus statut);
    boolean existsByNumero(String numero);
}