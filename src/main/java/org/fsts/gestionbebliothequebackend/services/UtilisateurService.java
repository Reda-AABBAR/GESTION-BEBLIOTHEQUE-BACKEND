package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.dtos.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UtilisateurService {
    UtilisateurDTO saveUtilisateur(UtilisateurDTO dto,String password);
    UtilisateurDTO updateUtilisateur(UUID id, UtilisateurDTO dto);
    UtilisateurDTO updateUtilisateurByEmail(String email, UtilisateurDTO dto);
    List<UtilisateurDTO> saveAllUtilisateur(List<UtilisateurDTO> dto, Map<String,String> passwords);
    UtilisateurDTO getUtilisateurByEmail(String email);
    UtilisateurDTO getUtilisateurById(UUID id);
    void deleteUtilisateurId(UUID id);
    List<UtilisateurDTO> getAllUtilisateur();

    Etudiant saveEtudiant(Etudiant dto, String password);
    Etudiant updateUtilisateur(UUID id, Etudiant dto);
    Etudiant updateEtudiantByEmail(String email, Etudiant dto);
    List<Etudiant> saveAllEtudiant(List<Etudiant> dto, Map<String,String> passwords);
    Etudiant getEtudiantByEmail(String email);
    Etudiant getEtudiantById(UUID id);
    void deleteEtudiantId(UUID id);
    void deleteListEtudiant(List<UUID> ids);
    List<Etudiant> getAllEtudiant();


    Bibliothecaire saveBibliothecaire(Bibliothecaire dto, String password);
    Bibliothecaire updateBibliothecaire(UUID id, Bibliothecaire dto);
    Bibliothecaire updateBibliothecaireByEmail(String email, Bibliothecaire dto);

    List<Bibliothecaire> saveAllBibliothecaire(List<Bibliothecaire> dto, Map<String,String> passwords);
    Bibliothecaire getBibliothecaireByEmail(String email);
    Bibliothecaire getBibliothecaireById(UUID id);
    void deleteBibliothecaireId(UUID id);
    void deleteListBibliothecaire(List<UUID> ids);
    List<Bibliothecaire> getAllBibliothecaire();


    Admin saveAdmin(Admin dto, String password);
    Admin updateAdmin(UUID id, Admin dto);
    Admin updateAdminByEmail(String email, Admin dto);

    List<Admin> saveAllAdmin(List<Admin> dto, Map<String,String> passwords);
    Admin getAdminByEmail(String email);
    Admin getAdminById(UUID id);
    void deleteAdminId(UUID id);
    void deleteAdmin(List<UUID> ids);
    List<Admin> getAllAdmin();


    Personnel savePersonnel(Personnel dto, String password);

    Personnel updatePersonnel(UUID id, Personnel dto);
    Personnel updatePersonnelByEmail(String email, Personnel dto);

    List<Personnel> saveAllPersonnel(List<Personnel> dto, Map<String,String> passwords);
    Personnel getPersonnelByEmail(String email);
    Personnel getPersonnelById(UUID id);
    void deletePersonnelId(UUID id);
    void deletePersonnel(List<UUID> ids);
    List<Personnel> getAllPersonnel();



    boolean isEmailExist(String email);
    int numberUtilisateur();
    int numberEtudiants();
    int numberPersonnels();
    int numberAdmins();
    int numberResponsables();
    int numberBibliothecaire();

}
