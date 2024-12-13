package org.fsts.gestionbebliothequebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.fsts.gestionbebliothequebackend.dtos.*;
import org.fsts.gestionbebliothequebackend.mappers.UtilisateurMapper;
import org.fsts.gestionbebliothequebackend.services.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateur")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class UtilisateurController {
    private final UtilisateurService service;
    @PostMapping("/save")
    public ResponseEntity<UtilisateurDTO> saveUtilisateur(@RequestBody UtilisateurDTO utilisateur,@RequestHeader String password){
        return ResponseEntity.ok(service.saveUtilisateur(utilisateur,password));
    }

    @PostMapping("/etudiant")
    public ResponseEntity<Etudiant> saveEtudiant(@RequestBody Etudiant etudiant, @RequestHeader String password){
        return ResponseEntity.ok(service.saveEtudiant(etudiant,password));
    }

    @PostMapping("/personnel")
    public ResponseEntity<Personnel> savePersonnel(@RequestBody Personnel personnel, @RequestHeader String password){
        return ResponseEntity.ok(service.savePersonnel(personnel,password));
    }

    @PostMapping("/admin")
    public ResponseEntity<Admin> saveAdmin(@RequestBody Admin admin, @RequestHeader String password){
        return ResponseEntity.ok(service.saveAdmin(admin,password));
    }

    @PostMapping("/bibliothecaire")
    public ResponseEntity<Bibliothecaire> saveBibliothecaire(@RequestBody Bibliothecaire bibliothecaire, @RequestHeader String password){
        return ResponseEntity.ok(service.saveBibliothecaire(bibliothecaire,password));
    }

    @PostMapping("/save/list")
    public ResponseEntity<List<UtilisateurDTO>> saveAllUtilisateur(@RequestBody Map<String,?> request){
        if (request == null) {
            return new ResponseEntity(null,HttpStatus.NOT_ACCEPTABLE);
        }
        List<UtilisateurDTO> utilisateurs = UtilisateurDTO.convertToUtilisateurDTOList(request.get("utilisateurs"));
        Map<String, String> passwords = (Map<String, String>) request.get("passwords");
        return ResponseEntity.ok(service.saveAllUtilisateur(utilisateurs,passwords));
    }

    @PostMapping("/save/list/etudiant")
    public ResponseEntity<List<Etudiant>> saveAllEtudiant(@RequestBody Map<String,?> request){
        if (request == null) {
            return new ResponseEntity(null,HttpStatus.NOT_ACCEPTABLE);
        }
        List<UtilisateurDTO> utilisateurs = UtilisateurDTO.convertToUtilisateurDTOList(request.get("utilisateurs"));
        List<Etudiant> etudiants = utilisateurs.stream().map(UtilisateurMapper::UtilisateurToEtudiant).toList();

        Map<String, String> passwords = (Map<String, String>) request.get("passwords");
        return ResponseEntity.ok(service.saveAllEtudiant(etudiants,passwords));
    }

    @PostMapping("/save/list/personnel")
    public ResponseEntity<List<Personnel>> saveAllPersonnel(@RequestBody Map<String,?> request){
        if (request == null) {
            return new ResponseEntity(null,HttpStatus.NOT_ACCEPTABLE);
        }
        List<UtilisateurDTO> utilisateurs = UtilisateurDTO.convertToUtilisateurDTOList(request.get("utilisateurs"));
        List<Personnel> personnels = utilisateurs.stream().map(UtilisateurMapper::UtilisateurToPersonnel).toList();

        Map<String, String> passwords = (Map<String, String>) request.get("passwords");
        return ResponseEntity.ok(service.saveAllPersonnel(personnels,passwords));
    }

    @PostMapping("/save/list/admin")
    public ResponseEntity<List<Admin>> saveAllAdmin(@RequestBody Map<String,?> request){
        if (request == null) {
            return new ResponseEntity(null,HttpStatus.NOT_ACCEPTABLE);
        }
        List<UtilisateurDTO> utilisateurs = UtilisateurDTO.convertToUtilisateurDTOList(request.get("utilisateurs"));
        List<Admin> admins = utilisateurs.stream().map(UtilisateurMapper::UtilisateurToAdmin).toList();

        Map<String, String> passwords = (Map<String, String>) request.get("passwords");
        return ResponseEntity.ok(service.saveAllAdmin(admins,passwords));
    }

    @PostMapping("/save/list/bibliothecaire")
    public ResponseEntity<List<Bibliothecaire>> saveAllBibliothecaire(@RequestBody Map<String,?> request){
        if (request == null) {
            return new ResponseEntity(null,HttpStatus.NOT_ACCEPTABLE);
        }
        List<UtilisateurDTO> utilisateurs = UtilisateurDTO.convertToUtilisateurDTOList(request.get("utilisateurs"));
        List<Bibliothecaire> bibliothecaires = utilisateurs.stream().map(UtilisateurMapper::UtilisateurToBibliothecaire).toList();

        Map<String, String> passwords = (Map<String, String>) request.get("passwords");
        return ResponseEntity.ok(service.saveAllBibliothecaire(bibliothecaires,passwords));
    }

    @GetMapping("/email/{email}")

    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable String email){
        return ResponseEntity.ok(service.getUtilisateurByEmail(email));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable UUID id){
        return ResponseEntity.ok(service.getUtilisateurById(id));
    }

    @GetMapping("/etudiant/email/{email}")

    public ResponseEntity<Etudiant> getEtudiant(@PathVariable String email){
        return ResponseEntity.ok(service.getEtudiantByEmail(email));
    }

    @GetMapping("/etudiant/id/{id}")
    public ResponseEntity<Etudiant> getEtudiant(@PathVariable UUID id){
        return ResponseEntity.ok(service.getEtudiantById(id));
    }

    @GetMapping("/personnel/email/{email}")

    public ResponseEntity<Personnel> getPersonnel(@PathVariable String email){
        return ResponseEntity.ok(service.getPersonnelByEmail(email));
    }

    @GetMapping("/admin/id/{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable UUID id){
        return ResponseEntity.ok(service.getAdminById(id));
    }

    @GetMapping("/admin/email/{email}")

    public ResponseEntity<Admin> getAdmin(@PathVariable String email){
        return ResponseEntity.ok(service.getAdminByEmail(email));
    }

    @GetMapping("/personnel/id/{id}")
    public ResponseEntity<Personnel> getPersonnel(@PathVariable UUID id){
        return ResponseEntity.ok(service.getPersonnelById(id));
    }

    @GetMapping()
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateur(){
        return ResponseEntity.ok(service.getAllUtilisateur());
    }

    @GetMapping("/etudiant")
    public ResponseEntity<List<Etudiant>> getAllEtudiant(){
        return ResponseEntity.ok(service.getAllEtudiant());
    }

    @GetMapping("/personnel")
    public ResponseEntity<List<Personnel>> getAllPersonnel(){
        return ResponseEntity.ok(service.getAllPersonnel());
    }

    @GetMapping("/bibliothecaire")
    public ResponseEntity<List<Bibliothecaire>> getAllBibliothecaire(){
        return ResponseEntity.ok(service.getAllBibliothecaire());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Admin>> getAllAdmin(){
        return ResponseEntity.ok(service.getAllAdmin());
    }

    @DeleteMapping("/{id}")
    public void deleteUtilisateur(@PathVariable UUID id){
        service.deleteUtilisateurId(id);
    }

    @DeleteMapping("/etudiant/{id}")
    public void deleteEtudiantId(@PathVariable UUID id){
        service.deleteEtudiantId(id);
    }
    @DeleteMapping("/personnel/{id}")
    public void deletePersonnelId(@PathVariable UUID id){
        service.deletePersonnelId(id);
    }
    @DeleteMapping("/bibliothecaire/{id}")
    public void deleteBibliothecaireId(@PathVariable UUID id){
        service.deleteBibliothecaireId(id);
    }
    @DeleteMapping("/admin/{id}")
    public void deleteAdminId(@PathVariable UUID id){
        service.deleteAdminId(id);
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

    @PutMapping("/etudiant/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable UUID id, @RequestBody Etudiant etudiant){
        return ResponseEntity.ok(service.updateUtilisateur(id,etudiant));
    }

    @PutMapping("/personnel/{id}")
    public ResponseEntity<Personnel> updatePersonnel(@PathVariable UUID id, @RequestBody Personnel personnel){
        return ResponseEntity.ok(service.updatePersonnel(id,personnel));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable UUID id, @RequestBody Admin admin){
        return ResponseEntity.ok(service.updateAdmin(id,admin));
    }

    @PutMapping("/bibliothecaire/{id}")
    public ResponseEntity<Bibliothecaire> updateBibliothecaire(@PathVariable UUID id, @RequestBody Bibliothecaire bibliothecaire){
        return ResponseEntity.ok(service.updateBibliothecaire(id,bibliothecaire));
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(@PathVariable String email, @RequestBody UtilisateurDTO utilisateur){
        return ResponseEntity.ok(service.updateUtilisateurByEmail(email,utilisateur));
    }

    @PutMapping("/email/etudiant/{email}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable String email, @RequestBody Etudiant etudiant){
        return ResponseEntity.ok(service.updateEtudiantByEmail(email,etudiant));
    }

    @PutMapping("/email/personnel/{email}")
    public ResponseEntity<Personnel> updatePersonnel(@PathVariable String email, @RequestBody Personnel personnel){
        return ResponseEntity.ok(service.updatePersonnelByEmail(email,personnel));
    }

    @PutMapping("/email/admin/{email}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable String email, @RequestBody Admin admin){
        return ResponseEntity.ok(service.updateAdminByEmail(email,admin));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<UtilisateurDTO> updatePassword(@PathVariable UUID id,@RequestHeader String newPassword){
        return ResponseEntity.ok(service.upDatePassword(id,newPassword));
    }

    @PutMapping("/password/email/{email}")
    public ResponseEntity<UtilisateurDTO> updatePasswordByEmail(@PathVariable String email,@RequestHeader String newPassword){
        return ResponseEntity.ok(service.upDatePasswordByEmail(email,newPassword));
    }

    @PutMapping("/email/bibliothecaire/{email}")
    public ResponseEntity<Bibliothecaire> updateBibliothecaire(@PathVariable String email, @RequestBody Bibliothecaire bibliothecaire){
        return ResponseEntity.ok(service.updateBibliothecaireByEmail(email,bibliothecaire));
    }

    @DeleteMapping("/list")
    public void deleteListUtilisateur(@RequestBody Map<String,List<UUID>> ids){
        List<UUID> listOfId= ids.get("ids");
        if (listOfId != null && !listOfId.isEmpty()) {
            service.deleteListUtilisateur(listOfId);
        }
    }

    @DeleteMapping("/etudiant/list")
    public void deleteEtudiantId(@RequestBody Map<String,List<UUID>> ids){
        List<UUID> listOfId= ids.get("ids");
        if (listOfId != null && !listOfId.isEmpty()) {
            service.deleteListEtudiant(listOfId);
        }
    }
    @DeleteMapping("/personnel/list")
    public void deletePersonnelId(@RequestBody Map<String,List<UUID>> ids){
        List<UUID> listOfId= ids.get("ids");
        if (listOfId != null && !listOfId.isEmpty()) {
            service.deleteListPersonnel(listOfId);
        }
    }
    @DeleteMapping("/bibliothecaire/list")
    public void deleteBibliothecaireId(@RequestBody Map<String,List<UUID>> ids){
        List<UUID> listOfId= ids.get("ids");
        if (listOfId != null && !listOfId.isEmpty()) {
            service.deleteListBibliothecaire(listOfId);
        }
    }
    @DeleteMapping("/admin/list")
    public void deleteAdminId(@RequestBody Map<String,List<UUID>> ids){
        List<UUID> listOfId= ids.get("ids");
        if (listOfId != null && !listOfId.isEmpty()) {
            service.deleteListAdmin(listOfId);
        }
    }

}
