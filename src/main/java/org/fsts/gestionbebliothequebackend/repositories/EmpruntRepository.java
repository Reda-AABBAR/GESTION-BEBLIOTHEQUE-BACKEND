package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
    @Query("SELECT e FROM Emprunt e WHERE e.statut = 'RETOURNER' AND e.dateRetour IS NOT NULL AND FUNCTION('MONTH', e.dateRetour) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', e.dateRetour) = FUNCTION('YEAR', CURRENT_DATE)")
    List<Emprunt> findEmpruntRetournerThisMonth();

    @Query("SELECT COUNT(e) FROM Emprunt e WHERE (e.statut = 'ATTENTE' OR e.statut = 'RETARD')")
    Long countEmpruntsActuels();

    @Query("SELECT e.document, COUNT(e) AS empruntCount " +
            "FROM Emprunt e " +
            "GROUP BY e.document " +
            "ORDER BY empruntCount DESC")
    List<Object[]> findTopDocumentsEmpruntes(Pageable pageable);

    int countByStatut(Emprunt.Statut statut);

    @Query("SELECT e FROM Emprunt e WHERE e.document.id = :documentId AND e.dateRetour IS NULL AND e.dateEmprunt <= :reservationDate")
    List<Emprunt> findEmpruntsByDocumentAndDate(
            @Param("documentId") Long documentId,
            @Param("reservationDate") Date reservationDate
    );
    int countByDocumentAndDateRetourIsNull(Document document);
    int countByUtilisateurAndDateEmprunt(Utilisateur utilisateur, Date dateEmprunt);

    @Query("SELECT FUNCTION('MONTH', e.dateRetour) AS mois, COUNT(e) AS nombreRetours " +
            "FROM Emprunt e " +
            "WHERE e.statut = 'RETOURNER' " +
            "GROUP BY FUNCTION('MONTH', e.dateRetour)")
    List<Object[]> countRetoursParMois();

    /*@Query("SELECT AVG(FUNCTION('DATEDIFF', e.dateRetour, e.dateEmprunt)) AS moyenneRetard " +
            "FROM Emprunt e " +
            "WHERE e.statut = 'RETARD'")
    Double findMoyenneTempsRetard();*/

    @Query("SELECT e FROM Emprunt e WHERE e.dateRetour IS NULL " +
            "AND e.dateEmprunt < :dateLimite " +
            "AND e.statut != 'RETOURNER'")
    List<Emprunt> findAllEmprintEnRetard(@Param("dateLimite") LocalDate dateLimite);



}
