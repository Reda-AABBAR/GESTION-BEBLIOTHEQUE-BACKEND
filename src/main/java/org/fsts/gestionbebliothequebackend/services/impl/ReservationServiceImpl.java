package org.fsts.gestionbebliothequebackend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsts.gestionbebliothequebackend.dtos.ReservationDTO;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Penalite;
import org.fsts.gestionbebliothequebackend.entities.Reservation;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.ReservationStatus;
import org.fsts.gestionbebliothequebackend.mappers.ReservationMapper;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.repositories.EmpruntRepository;
import org.fsts.gestionbebliothequebackend.repositories.ReservationRepository;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.fsts.gestionbebliothequebackend.services.NotificationProviderService;
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
    private final NotificationProviderService notificationProviderService;
    private final EmpruntRepository empruntRepository;
    private final PenaliteServiceImpl penaliteServiceImpl;


    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);

        // Check if exemplaire exists for the selected reservation date
        if (!isExemplaireExistForReservation(reservation)) {
            throw new IllegalStateException("Pas d'exemplaire pour ce document pour cette date");
        }

        // Ensure the reservation date is not in the past
        if (reservation.getDateReservation().before(new Date())) {
            throw new IllegalArgumentException("La date de réservation ne peut pas être dans le passé.");
        }

        // Check if the user has any active penalties for the reservation date
        if (hasActivePenalite(reservation.getUtilisateur(), reservation.getDateReservation())) {
            throw new IllegalArgumentException("Vous ne pouvez pas effectuer une réservation pendant la période de pénalité.");
        }

        // Check if the reservation date is valid based on user's previous reservations
        reservation.setReservationStatus(ReservationStatus.ENCOURS);
        if (!peutReserver(reservation.getUtilisateur().getId(), reservation.getDateReservation())) {
            throw new IllegalArgumentException("La Date de réservation est invalide");
        }

        // Check if the user has already made two confirmed reservations in the past 3 days
        Date threeDaysAgo = Date.from(reservation.getDateReservation().toInstant().minusSeconds(3 * 24 * 60 * 60));
        int confirmedReservationsCount = reservationRepository.countConfirmedReservationsWithinDateRange(
                reservation.getUtilisateur().getId(), threeDaysAgo, reservation.getDateReservation());
        if (confirmedReservationsCount >= 2) {
            throw new IllegalArgumentException("Vous ne pouvez pas effectuer de nouvelle réservation dans les 3 jours suivant deux réservations confirmées.");
        }

        // Save the reservation
        reservation = reservationRepository.save(reservation);
        log.info("Created reservation with ID: {}", reservation.getId());

        // Notify others about the reservation
        notificationProviderService.alertDocumentReservedToAllBibliocathere(reservation.getDocument());

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
        // Find the existing reservation by ID
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found for ID: " + id));

        Document document;
        Utilisateur utilisateur;

        // Update the reservation date and check if it is not in the future
        if (existingReservation.getDateReservation().compareTo(reservationDTO.dateReservation()) != 0 && reservationDTO.dateReservation().before(new Date())) {
            throw new IllegalArgumentException("La date de réservation ne peut pas être dans le passé.");
        }
        existingReservation.setDateReservation(reservationDTO.dateReservation());

        // Update the utilisateur if a new utilisateurId is provided
        if (reservationDTO.utilisateurId() != null) {
            utilisateur = utilisateurRepository.findById(reservationDTO.utilisateurId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + reservationDTO.utilisateurId()));
            existingReservation.setUtilisateur(utilisateur);
        }

        // Update the document if a new documentId is provided
        if (reservationDTO.documentId() != null) {
            document = documentRepository.findById(reservationDTO.documentId())
                    .orElseThrow(() -> new IllegalArgumentException("Document not found for ID: " + reservationDTO.documentId()));
            existingReservation.setDocument(document);
        }

        // Ensure the exemplaire exists for the selected reservation date
        if (!isExemplaireExistForReservation(existingReservation)) {
            throw new IllegalStateException("No exemplaires available for the document on the selected reservation date.");
        }

        // Ensure the user has no more than two confirmed reservations within the past 3 days
        Date threeDaysAgo = Date.from(existingReservation.getDateReservation().toInstant().minusSeconds(3 * 24 * 60 * 60));
        int confirmedReservationsCount = reservationRepository.countConfirmedReservationsWithinDateRange(
                existingReservation.getUtilisateur().getId(), threeDaysAgo, existingReservation.getDateReservation());
        if (confirmedReservationsCount >= 2) {
            throw new IllegalArgumentException("Vous ne pouvez pas effectuer de nouvelle réservation dans les 3 jours suivant deux réservations confirmées.");
        }

        // Update the reservation status if changed and notify
        if (!existingReservation.getReservationStatus().equals(reservationDTO.reservationStatus())) {
            existingReservation.setReservationStatus(reservationDTO.reservationStatus());
            notificationProviderService.alertReservationStatusHasBeenChanged(existingReservation);
        }

        // Save the updated reservation
        existingReservation = reservationRepository.save(existingReservation);
        log.info("Updated reservation with ID: {}", existingReservation.getId());

        // Return the updated reservation as DTO
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

    @Override
    public List<ReservationDTO> getReservationsByUser(UUID utilisateurID) {
        return reservationRepository.findReservationsByUtilisateurId(utilisateurID)
                .stream().map(
                        reservationMapper::toDTO
                ).toList();
    }

    public boolean peutReserver(UUID utilisateur_Id, Date dateReservation) {
        List<Penalite> penalites = penaliteServiceImpl.getPenalitesByUtilisateur(utilisateur_Id);

        for (Penalite penalite : penalites) {
            Date dateFinPenalite = penalite.getDateFin();

            if ((dateReservation.before(dateFinPenalite) && dateReservation.after(penalite.getDateDebut()))) {
                return false;
            }
        }
        return true;
    }
        private boolean isExemplaireExistForReservation (Reservation reservation){
            Document document = reservation.getDocument();

            int totalExemplaires = document.getNbrExemplaire();

            int activeReservations = reservationRepository.findReservationsByDocumentAndDate(
                    document.getId(), reservation.getDateReservation()
            ).size();

            int activeEmprunts = empruntRepository.findEmpruntsByDocumentAndDate(
                    document.getId(), reservation.getDateReservation()
            ).size();
            return totalExemplaires - activeReservations - activeEmprunts > 0;
        }

    private boolean hasActivePenalite(Utilisateur utilisateur, Date reservationDate) {
        // Fetch the user's penalties
        List<Penalite> penalites = utilisateur.getPenalites();

        // Iterate through the penalties to check if any are active during the reservation date
        for (Penalite penalite : penalites) {
            Date penaliteStartDate = penalite.getDateDebut();
            Date penaliteEndDate = penalite.getDateFin();

            if (penaliteStartDate != null && penaliteEndDate != null) {
                // If the reservation date falls within the penalty period, return true
                if (!reservationDate.before(penaliteStartDate) && !reservationDate.after(penaliteEndDate)) {
                    return true;  // There is an active penalty for this user during the reservation date
                }
            }
        }
        // No active penalties found for this user during the reservation date
        return false;
    }

}