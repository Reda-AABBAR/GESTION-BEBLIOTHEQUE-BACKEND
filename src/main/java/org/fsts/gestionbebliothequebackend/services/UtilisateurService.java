package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.dtos.UtilisateurDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UtilisateurService {
    UtilisateurDTO saveUtilisateur(UtilisateurDTO dto,String password);
    UtilisateurDTO updateUtilisateur(UUID id, UtilisateurDTO dto);
    List<UtilisateurDTO> saveAllUtilisateur(List<UtilisateurDTO> dto, Map<String,String> passwords);
    UtilisateurDTO getUtilisateurByEmail(String email);
    UtilisateurDTO getUtilisateurById(UUID id);
    void deleteUtilisateurId(UUID id);
    boolean isEmailExist(String email);
    int numberUtilisateur();
    int numberEtudiants();
    int numberPersonnels();
    int numberAdmins();
    int numberResponsables();
    int numberBibliothecaire();
}
