package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Reservation;
import org.fsts.gestionbebliothequebackend.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("SELECT r FROM Reservation r WHERE r.utilisateur.id = :utilisateurId")
    List<Reservation> findReservationsByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);

    @Query("SELECT r FROM Reservation r WHERE r.document.id = :documentId AND r.dateReservation = :reservationDate AND r.reservationStatus = 'ACCEPTED'")
    List<Reservation> findReservationsByDocumentAndDate(
            @Param("documentId") Long documentId,
            @Param("reservationDate") Date reservationDate
    );

    int countByDocumentAndDateReservationAfterAndReservationStatus(Document document, Date dateReservation, ReservationStatus reservationStatus);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.utilisateur.id = :utilisateurId AND r.dateReservation = :reservationDate")
    int countReservationsByUtilisateurAndDate(
            @Param("utilisateurId") UUID utilisateurId,
            @Param("reservationDate") Date reservationDate
    );

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.utilisateur.id = :utilisateurId " +
            "AND r.reservationStatus = 'ACCEPTED' " +
            "AND r.dateReservation BETWEEN :startDate AND :endDate")
    int countConfirmedReservationsWithinDateRange(
            @Param("utilisateurId") UUID utilisateurId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);


}
