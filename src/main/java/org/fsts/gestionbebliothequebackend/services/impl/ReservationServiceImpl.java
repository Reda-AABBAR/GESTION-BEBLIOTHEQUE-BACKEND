package org.fsts.gestionbebliothequebackend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsts.gestionbebliothequebackend.dtos.ReservationDTO;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Reservation;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.mappers.ReservationMapper;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.repositories.ReservationRepository;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.fsts.gestionbebliothequebackend.services.ReservationService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UtilisateurRepository utilisateurRepository;
    private final DocumentRepository documentRepository;


    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation.setDateReservation(new Date());
        reservation = reservationRepository.save(reservation);
        log.info("Created reservation with ID: {}", reservation.getId());
        return reservationMapper.toDTO(reservation);
    }

    @Override
    public ReservationDTO getReservationById(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found for ID: " + id));
        return reservationMapper.toDTO(reservation);
    }

    @Override
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDTO updateReservation(UUID id, ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found for ID: " + id));
        Document document;
        Utilisateur utilisateur;

        existingReservation.setDateReservation(reservationDTO.dateReservation());

        if (reservationDTO.utilisateurId() != null) {
            utilisateur = utilisateurRepository.findById(reservationDTO.utilisateurId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + id));
            existingReservation.setUtilisateur(utilisateur);
        }

        if (reservationDTO.documentId() != null) {
            document = documentRepository.findById(reservationDTO.documentId())
                    .orElseThrow(() -> new IllegalArgumentException("Document not found for ID: " + id));
            existingReservation.setDocument(document);
        }

        existingReservation = reservationRepository.save(existingReservation);
        log.info("Updated reservation with ID: {}", existingReservation.getId());
        return reservationMapper.toDTO(existingReservation);
    }

    @Override
    public void deleteReservation(UUID id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation not found for ID: " + id);
        }
        reservationRepository.deleteById(id);
        log.info("Deleted reservation with ID: {}", id);
    }
}
