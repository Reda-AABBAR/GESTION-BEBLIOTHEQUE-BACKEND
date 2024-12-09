package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("SELECT r FROM Reservation r WHERE r.utilisateur.id = :utilisateurId")
    List<Reservation> findReservationsByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);

}
