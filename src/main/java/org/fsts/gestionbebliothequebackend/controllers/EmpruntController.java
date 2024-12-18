package org.fsts.gestionbebliothequebackend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.services.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/emprunts")
@RequiredArgsConstructor
public class EmpruntController {

    private final EmpruntService empruntService;

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

    @GetMapping("/retard")
    public List<Emprunt> getEmpruntsRetard() {
        return empruntService.getEmpruntsRetard();
    }

    @GetMapping("/retourner")
    public List<Emprunt> getEmpruntsRetourner() {
        return empruntService.getEmpruntsRetourner();
    }
    @GetMapping("/retourneravecretarder")
    public List<Emprunt> getEmpruntsretourneravecretarder(){
        return empruntService.getEmpruntsWithDelay();
    }

    @GetMapping("/retournersansretarder")
    public List<Emprunt> getEmpruntsretournersansretarder(){
        return empruntService.getEmpruntsWithoutDelay();
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Emprunt> updateStatut(
            @PathVariable Long id,
                @RequestParam Emprunt.Statut nouveauStatut) {
        Emprunt updatedEmprunt = empruntService.updateStatut(id, nouveauStatut);
        return ResponseEntity.ok(updatedEmprunt);
    }

    @GetMapping("/statut")
    public ResponseEntity<List<Emprunt>> getByStatut(@RequestParam Emprunt.Statut statut) {
        List<Emprunt> emprunts = empruntService.getByStatut(statut);
        return ResponseEntity.ok(emprunts);
    }

    @GetMapping
    public ResponseEntity<List<Emprunt>> getAllEmprunts() {
        List<Emprunt> emprunts = empruntService.getAllEmprunts();
        return ResponseEntity.ok(emprunts);
    }

    @GetMapping("/utilisateur/{utilisateurId}/is-valid")
        public ResponseEntity<Boolean> isEmpruntCountValid(@PathVariable UUID utilisateurId) {
        boolean isValid = empruntService.isEmpruntCountValid(utilisateurId);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/count-this-month")
    public Long getEmpruntsCountThisMonth() {
        return empruntService.getEmpruntsCountThisMonth();
    }

    @GetMapping("/emprunts/en-cours")
    public List<Emprunt> getEmpruntsEnCours() {
        return empruntService.getEmpruntsEnCours();
    }

    @GetMapping("/documents/statistiques") // elle retourn les livres disponible et emprunter actuellement
    public Map<String, Long> getStatistiquesLivres() {
        return empruntService.getStatistiquesLivres();
    }

    @GetMapping("/top-documents")
    public List<Map<String, Object>> getTop30DocumentsEmpruntes() {
        return empruntService.getTop30DocumentsEmpruntes();
    }

    @GetMapping("/retours-par-mois")
    public ResponseEntity<Map<Integer, Long>> getNombreRetoursParMois() {
        Map<Integer, Long> retoursParMois = empruntService.getNombreRetoursParMois();
        return ResponseEntity.ok(retoursParMois);
    }
    /*@GetMapping("/moyenne-retard")
    public ResponseEntity<Double> getMoyenneTempsRetard() {
        Double moyenneRetard = empruntService.getMoyenneTempsRetard();
        return ResponseEntity.ok(moyenneRetard);
    }*/
    @GetMapping("/retards")
    public ResponseEntity<List<Emprunt>> getEmprintsEnRetard() {
        List<Emprunt> retards = empruntService.getAllEmprintRetard();
        return ResponseEntity.ok(retards);
    }
}