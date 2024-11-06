package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
}
