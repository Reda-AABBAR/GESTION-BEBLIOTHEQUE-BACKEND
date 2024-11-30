package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.repositories.EmpruntRepository;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//package org.fsts.gestionbebliothequebackend.services;

import java.util.UUID;

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
            // Additional fields can be set here if needed
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

}