package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.dtos.ReservationDTO;
import java.util.List;
import java.util.UUID;

public interface ReservationService {
    ReservationDTO createReservation(ReservationDTO reservationDTO);
    ReservationDTO getReservationById(UUID id);
    List<ReservationDTO> getAllReservations();
    ReservationDTO updateReservation(UUID id, ReservationDTO reservationDTO);
    void deleteReservation(UUID id);
    List<ReservationDTO> getReservationsByUser(UUID utilisateurID);
}
