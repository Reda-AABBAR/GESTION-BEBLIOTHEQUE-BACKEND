package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EmpruntRepository extends JpaRepository<Emprunt,Long> {
    List<Emprunt> findByDocument(Document document);
    List<Emprunt> findByUtilisateur(Utilisateur utilisateur);

    List<Emprunt> findByDocumentStatut(Document.Statut statut);

    List<Emprunt> findByDocumentStatutAndDateRetourBefore(Document.Statut statut, Date dateRetour);

    int countByUtilisateurId(Long utilisateurId);
    List<Emprunt> findByStatut(Emprunt.Statut statut);

}
