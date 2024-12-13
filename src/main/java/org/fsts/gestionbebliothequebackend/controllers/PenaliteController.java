package org.fsts.gestionbebliothequebackend.controllers;

import org.fsts.gestionbebliothequebackend.entities.Penalite;
import org.fsts.gestionbebliothequebackend.services.PenaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

    @RestController
    @RequestMapping("/api/penalites")
    public class PenaliteController {

        @Autowired
        private PenaliteService penaliteService;

        @PostMapping
        public ResponseEntity<Penalite> saveOrUpdatePenalite(@RequestBody Penalite penalite) {
            Penalite savedPenalite = penaliteService.ajouterouUpdatePenalite(penalite);
            return ResponseEntity.ok(savedPenalite);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Penalite> getPenaliteById(@PathVariable UUID id) {
            Optional<Penalite> penalite = penaliteService.getPenaliteById(id);
            return penalite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @GetMapping
        public ResponseEntity<List<Penalite>> getAllPenalites() {
            List<Penalite> penalites = penaliteService.getAllPenalites();
            return ResponseEntity.ok(penalites);
        }

        @GetMapping("/utilisateur/{utilisateurId}")
        public ResponseEntity<List<Penalite>> getPenalitesByUtilisateur(@PathVariable UUID utilisateurId) {
            List<Penalite> penalites = penaliteService.getPenalitesByUtilisateur(utilisateurId);
            return ResponseEntity.ok(penalites);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePenaliteById(@PathVariable UUID id) {
            penaliteService.deletePenaliteById(id);
            return ResponseEntity.noContent().build();
        }
    }