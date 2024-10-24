package org.fsts.gestionbebliothequebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.fsts.gestionbebliothequebackend.dtos.UtilisateurDTO;
import org.fsts.gestionbebliothequebackend.services.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateur")
@RequiredArgsConstructor
public class UtilisateurController {
    private final UtilisateurService service;
    @PostMapping("/save")
    public ResponseEntity<UtilisateurDTO> saveUtilisateur(@RequestBody UtilisateurDTO utilisateur){
        return ResponseEntity.ok(service.saveUtilisateur(utilisateur));
    }

    @PostMapping("/save/list")
    public ResponseEntity<List<UtilisateurDTO>> saveAllUtilisateur(@RequestBody List<UtilisateurDTO> utilisateur){
        return ResponseEntity.ok(service.saveAllUtilisateur(utilisateur));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable String email){
        return ResponseEntity.ok(service.getUtilisateurByEmail(email));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable UUID id){
        return ResponseEntity.ok(service.getUtilisateurById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUtilisateur(@PathVariable UUID id){
        service.deleteUtilisateurId(id);
    }

    @GetMapping("/count/utilisateur")
    public ResponseEntity<Map<String,Integer>> countUtilisateur(){
        return ResponseEntity.ok(Map.of(
                "utilisateurs",service.numberUtilisateur(),
                "admins",service.numberAdmins(),
                "responsables",service.numberResponsables(),
                "personnels",service.numberPersonnels(),
                "bibliothecaire",service.numberBibliothecaire(),
                "etudiants",service.numberEtudiants()
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(@PathVariable UUID id, @RequestBody UtilisateurDTO utilisateur){
        return ResponseEntity.ok(service.updateUtilisateur(id,utilisateur));
    }

}
