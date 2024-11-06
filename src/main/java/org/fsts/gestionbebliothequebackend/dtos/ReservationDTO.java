package org.fsts.gestionbebliothequebackend.dtos;
import java.util.Date;
import java.util.UUID;

public record ReservationDTO(
        UUID id,
        UUID utilisateurId,
        Long documentId,
        Date dateReservation
) {}
