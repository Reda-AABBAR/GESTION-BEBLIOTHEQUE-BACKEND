package org.fsts.gestionbebliothequebackend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.services.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/emprunts")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;

    @PostMapping("/save")
    public ResponseEntity<Emprunt> saveEmprunt(@RequestParam UUID utilisateurId, @RequestParam Long documentId, @RequestBody Emprunt emprunt) {
        try {
            Emprunt savedEmprunt = empruntService.saveEmprunt(utilisateurId, documentId, emprunt);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmprunt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmprunt(@PathVariable Long id) {
        try {
            empruntService.deleteEmprunt(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Emprunt deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Emprunt> updateEmprunt(@PathVariable Long id, @RequestBody Emprunt updatedEmprunt) {
        try {
            Emprunt emprunt = empruntService.updateEmprunt(id, updatedEmprunt);
            return ResponseEntity.ok(emprunt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/by-document/{documentId}")
    public List<Emprunt> getEmpruntsByDocument(@PathVariable Long documentId) {
        Document document = new Document(); // Assuming you have a way to fetch or create Document here
        document.setId(documentId);
        return empruntService.getAllEmpruntsByDocument(document);
    }

    @GetMapping("/by-utilisateur/{utilisateurId}")
    public List<Emprunt> getEmpruntsByUtilisateur(@PathVariable UUID utilisateurId) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(utilisateurId);
        return empruntService.getAllEmpruntsByUtilisateur(utilisateur);
    }
}