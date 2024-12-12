package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EmpruntRepository extends JpaRepository<Emprunt,Long> {
    List<Emprunt> findByDocument(Document document);
    List<Emprunt> findByUtilisateur(Utilisateur utilisateur);

    List<Emprunt> findByDocumentStatut(Document.Statut statut);

    List<Emprunt> findByDocumentStatutAndDateRetourBefore(Document.Statut statut, Date dateRetour);

    int countByUtilisateurId(UUID utilisateurId);
    List<Emprunt> findByStatut(Emprunt.Statut statut);

    @Query("SELECT COUNT(e) FROM Emprunt e WHERE MONTH(e.dateEmprunt) = MONTH(:currentDate) AND YEAR(e.dateEmprunt) = YEAR(:currentDate)")
    Long countEmpruntsByMonth(@Param("currentDate") Date currentDate);

    @Query("SELECT e FROM Emprunt e WHERE (e.statut = 'ATTENTE' OR e.statut = 'RETARD') AND e.dateRetour IS NULL")
    List<Emprunt> findEmpruntsEnCours();

    @Query("SELECT COUNT(e) FROM Emprunt e WHERE (e.statut = 'ATTENTE' OR e.statut = 'RETARD')")
    Long countEmpruntsActuels();

    @Query("SELECT e.document, COUNT(e) AS empruntCount " +
            "FROM Emprunt e " +
            "GROUP BY e.document " +
            "ORDER BY empruntCount DESC")
    List<Object[]> findTopDocumentsEmpruntes(Pageable pageable);

    @Query("SELECT FUNCTION('MONTH', e.dateRetour) AS mois, COUNT(e) AS nombreRetours " +
            "FROM Emprunt e " +
            "WHERE e.statut = 'RETOURNER' " +
            "GROUP BY FUNCTION('MONTH', e.dateRetour)")
    List<Object[]> countRetoursParMois();

    @Query("SELECT AVG(FUNCTION('DATEDIFF', e.dateRetour, e.dateEmprunt)) AS moyenneRetard " +
            "FROM Emprunt e " +
            "WHERE e.statut = 'RETARD'")
    Double findMoyenneTempsRetard();


}
