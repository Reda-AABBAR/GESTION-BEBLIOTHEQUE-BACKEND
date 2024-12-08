package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.repositories.EmpruntRepository;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class EmpruntService {
    @Autowired
    private final EmpruntRepository empruntRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public EmpruntService(EmpruntRepository empruntRepository) {
        this.empruntRepository = empruntRepository;
    }

    public List<Emprunt> getAllEmpruntsByDocument(Document document) {
        return empruntRepository.findByDocument(document);
    }

    public List<Emprunt> getAllEmpruntsByUtilisateur(Utilisateur utilisateur) {
        return empruntRepository.findByUtilisateur(utilisateur);
    }
    public Emprunt saveEmprunt(UUID utilisateurId, Long documentId, Emprunt emprunt) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(utilisateurId);
        Optional<Document> document = documentRepository.findById(documentId);

        if (utilisateur.isPresent() && document.isPresent()) {//cheching if it exist
            emprunt.setUtilisateur(utilisateur.get());
            emprunt.setDocument(document.get());
            return empruntRepository.save(emprunt);
        } else {
            throw new IllegalArgumentException("Invalid Document or Utilisateur ID.");
        }
    }

    public void deleteEmprunt(Long empruntId) {
        if (empruntRepository.existsById(empruntId)) {
            empruntRepository.deleteById(empruntId);
        } else {
            throw new IllegalArgumentException("Emprunt with ID " + empruntId + " does not exist.");
        }
    }

    public Emprunt updateEmprunt(Long empruntId, Emprunt updatedEmprunt) {
        Optional<Emprunt> existingEmprunt = empruntRepository.findById(empruntId);

        if (existingEmprunt.isPresent()) {
            Emprunt emprunt = existingEmprunt.get();
            emprunt.setDateEmprunt(updatedEmprunt.getDateEmprunt());
            emprunt.setDateRetour(updatedEmprunt.getDateRetour());
            emprunt.setStatut(updatedEmprunt.getStatut());
            return empruntRepository.save(emprunt);
        } else {
            throw new IllegalArgumentException("Emprunt with ID " + empruntId + " does not exist.");
        }
    }
    public List<Emprunt> getEmpruntsRetard() {
        Date currentDate = new Date();
        return empruntRepository.findByDocumentStatutAndDateRetourBefore(Document.Statut.NOT_EXIST, currentDate);
    }
    public List<Emprunt> getEmpruntsRetourner() {
        return empruntRepository.findByDocumentStatut(Document.Statut.EXIST);
    }

    public List<Emprunt> getEmpruntsWithDelay() {

        List<Emprunt> allEmprunts = empruntRepository.findByDocumentStatut(Document.Statut.EXIST);

        List<Emprunt> overdueEmprunts = new ArrayList<>();

        for (Emprunt emprunt : allEmprunts) {
            if (emprunt.getDateRetour() != null) {
                // calculer la date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(emprunt.getDateRetour());
                calendar.add(Calendar.DAY_OF_MONTH, 3); // ajouter 3 jours
                Date dateLimite = calendar.getTime();

                if (emprunt.getDateRetour().after(dateLimite)) {
                    overdueEmprunts.add(emprunt);
                }
            }
        }
        return overdueEmprunts;
    }

    public List<Emprunt> getEmpruntsWithoutDelay(){
        List<Emprunt> allEmprunts = empruntRepository.findByDocumentStatut(Document.Statut.EXIST);

        List<Emprunt> notoverdueEmprunts = new ArrayList<>();

        for (Emprunt emprunt : allEmprunts) {
            if (emprunt.getDateRetour() != null) {
                // calculer la date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(emprunt.getDateRetour());
                calendar.add(Calendar.DAY_OF_MONTH, 3); // ajouter 3 jours
                Date dateLimite = calendar.getTime();

                if (dateLimite.after(emprunt.getDateRetour())) {
                    notoverdueEmprunts.add(emprunt);
                }
            }
        }
        return notoverdueEmprunts;
    }

    public Emprunt updateStatut(Long id, Emprunt.Statut nouveauStatut) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable avec l'ID : " + id));
        emprunt.setStatut(nouveauStatut);
        return empruntRepository.save(emprunt);
    }

    public List<Emprunt> getByStatut(Emprunt.Statut statut) {
        return empruntRepository.findByStatut(statut);
    }

    public List<Emprunt> getAllEmprunts() {
        return empruntRepository.findAll();
    }

    public boolean isEmpruntCountValid(UUID utilisateurId) {
        int empruntCount = empruntRepository.countByUtilisateurId(utilisateurId);
        return empruntCount <= 2;
    }

}