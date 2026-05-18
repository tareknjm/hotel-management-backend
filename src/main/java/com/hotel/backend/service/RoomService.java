package com.hotel.backend.service;

import com.hotel.backend.dto.RoomRequest;
import com.hotel.backend.dto.RoomResponse;
import com.hotel.backend.entity.Room;
import com.hotel.backend.entity.RoomStatus;
import com.hotel.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<RoomResponse> getAll() {
        return roomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RoomResponse> getDisponibles() {
        return roomRepository.findByStatut(RoomStatus.DISPONIBLE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RoomResponse getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre introuvable"));
        return toResponse(room);
    }

    public RoomResponse create(RoomRequest request) {
        if (roomRepository.existsByNumero(request.getNumero())) {
            throw new RuntimeException("Numéro de chambre déjà utilisé");
        }

        Room room = Room.builder()
                .numero(request.getNumero())
                .type(request.getType())
                .etage(request.getEtage())
                .prixParNuit(request.getPrixParNuit())
                .statut(RoomStatus.DISPONIBLE)
                .build();

        return toResponse(roomRepository.save(room));
    }

    public RoomResponse update(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre introuvable"));

        room.setNumero(request.getNumero());
        room.setType(request.getType());
        room.setEtage(request.getEtage());
        room.setPrixParNuit(request.getPrixParNuit());

        return toResponse(roomRepository.save(room));
    }

    public RoomResponse updateStatut(Long id, RoomStatus statut) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre introuvable"));
        room.setStatut(statut);
        return toResponse(roomRepository.save(room));
    }

    public void delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Chambre introuvable");
        }
        roomRepository.deleteById(id);
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getNumero(),
                room.getType(),
                room.getEtage(),
                room.getPrixParNuit(),
                room.getStatut()
        );
    }
}