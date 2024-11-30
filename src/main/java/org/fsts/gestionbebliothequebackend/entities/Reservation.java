package org.fsts.gestionbebliothequebackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.fsts.gestionbebliothequebackend.enums.ReservationStatus;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Document document;

    @Temporal(TemporalType.DATE)
    private Date dateReservation;
    private ReservationStatus reservationStatus;
}

