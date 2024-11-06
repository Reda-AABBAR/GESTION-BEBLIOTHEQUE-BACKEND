package org.fsts.gestionbebliothequebackend.mappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsts.gestionbebliothequebackend.dtos.ReservationDTO;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Reservation;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class ReservationMapper {

    private final UtilisateurRepository utilisateurRepository;
    private final DocumentRepository documentRepository;
    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return new ReservationDTO(
                reservation.getId(),
                reservation.getUtilisateur() != null ? reservation.getUtilisateur().getId() : null,
                reservation.getDocument() != null ? reservation.getDocument().getId() : null,
                reservation.getDateReservation()
        );
    }
    public Reservation toEntity(ReservationDTO reservationDTO) {
        if (reservationDTO == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.id());
        if (reservationDTO.utilisateurId() != null) {
            Utilisateur utilisateur = utilisateurRepository.findById(reservationDTO.utilisateurId())
                    .orElseThrow(
                            ()->{
                                log.error("No user for id {}",reservationDTO.utilisateurId());
                                return null;
                            }
                    );
            reservation.setUtilisateur(utilisateur);
        }

        if (reservationDTO.documentId() != null) {
            Document document = documentRepository.findById(reservationDTO.documentId())
                            .orElseThrow(
                                    ()->{
                                        log.error("No document for id {}",reservationDTO.utilisateurId());
                                        return null;
                                    }
                            );
            reservation.setDocument(document);
        }
        reservation.setDateReservation(reservationDTO.dateReservation());
        return reservation;
    }
}

