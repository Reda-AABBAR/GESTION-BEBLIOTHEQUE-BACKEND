package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Query("SELECT COUNT(d) FROM Document d WHERE d.id NOT IN (SELECT e.document.id FROM Emprunt e WHERE e.statut = 'ATTENTE' OR e.statut = 'RETARD')")
    Long countDocumentsDisponibles();
}
