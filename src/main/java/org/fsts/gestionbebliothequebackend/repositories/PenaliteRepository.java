package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.entities.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PenaliteRepository extends JpaRepository<Penalite, UUID> {
    List<Penalite> findByUtilisateurId(UUID utilisateurId);
}
