package org.fsts.gestionbebliothequebackend.mappers;

import org.fsts.gestionbebliothequebackend.dtos.*;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;
import org.springframework.context.annotation.Bean;

public class UtilisateurMapper {
    public static UtilisateurDTO toDto(Utilisateur entity){
        return UtilisateurDTO.builder()
                .id(entity.getId())
                .role(entity.getRole())
                .email(entity.getEmail())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .code(entity.getCode())
                .notifications(entity.getNotifications())
                .build();
    }
    public static Utilisateur toEntity(UtilisateurDTO entity){
        return Utilisateur.builder()
                .id(entity.id())
                .role(entity.role())
                .email(entity.email())
                .code(entity.code())
                .nom(entity.nom())
                .prenom(entity.prenom())
                .notifications(entity.notifications())
                .build();
    }

    public static UtilisateurDTO EtudiantToUtilisateurDTO(Etudiant etudiant){
        return UtilisateurDTO.builder()
                .id(etudiant.id())
                .role(UtilisateurRole.ETUDIANT)
                .email(etudiant.email())
                .nom(etudiant.nom())
                .prenom(etudiant.prenom())
                .code(etudiant.codeMassar())
                .notifications(etudiant.notifications())
                .build();
    }

    public static Etudiant UtilisateurToEtudiant(UtilisateurDTO dto){
        return Etudiant.builder()
                .codeMassar(dto.code())
                .email(dto.email())
                .id(dto.id())
                .nom(dto.nom())
                .prenom(dto.prenom())
                .notifications(dto.notifications())
                .build();
    }

    public static UtilisateurDTO PersonnelToUtilisateurDTO(Personnel personnel){
        return UtilisateurDTO.builder()
                .id(personnel.id())
                .role(UtilisateurRole.PERSONNEL)
                .email(personnel.email())
                .nom(personnel.nom())
                .prenom(personnel.prenom())
                .code(personnel.code())
                .notifications(personnel.notifications())
                .build();
    }

    public static Personnel UtilisateurToPersonnel(UtilisateurDTO dto){
        return Personnel.builder()
                .code(dto.code())
                .email(dto.email())
                .id(dto.id())
                .nom(dto.nom())
                .prenom(dto.prenom())
                .notifications(dto.notifications())
                .build();
    }

    public static UtilisateurDTO AdminToUtilisateurDTO(Admin admin){
        return UtilisateurDTO.builder()
                .id(admin.id())
                .role(UtilisateurRole.ADMIN)
                .email(admin.email())
                .nom(admin.nom())
                .prenom(admin.prenom())
                .code(admin.code())
                .notifications(admin.notifications())
                .build();
    }

    public static Admin UtilisateurToAdmin(UtilisateurDTO dto){
        return Admin.builder()
                .code(dto.code())
                .email(dto.email())
                .id(dto.id())
                .nom(dto.nom())
                .prenom(dto.prenom())
                .notifications(dto.notifications())
                .build();
    }

    public static UtilisateurDTO BibliothecaireToUtilisateurDTO(Bibliothecaire bibliothecaire){
        return UtilisateurDTO.builder()
                .id(bibliothecaire.id())
                .role(UtilisateurRole.BIBLIOTHECAIRE)
                .email(bibliothecaire.email())
                .nom(bibliothecaire.nom())
                .prenom(bibliothecaire.prenom())
                .code(bibliothecaire.code())
                .notifications(bibliothecaire.notifications())
                .build();
    }

    public static Bibliothecaire UtilisateurToBibliothecaire(UtilisateurDTO dto){
        return Bibliothecaire.builder()
                .code(dto.code())
                .email(dto.email())
                .id(dto.id())
                .nom(dto.nom())
                .prenom(dto.prenom())
                .notifications(dto.notifications())
                .build();
    }

}
