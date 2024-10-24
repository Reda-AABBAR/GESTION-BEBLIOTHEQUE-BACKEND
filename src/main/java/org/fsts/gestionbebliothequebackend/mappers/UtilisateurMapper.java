package org.fsts.gestionbebliothequebackend.mappers;

import org.fsts.gestionbebliothequebackend.dtos.UtilisateurDTO;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.springframework.context.annotation.Bean;

public class UtilisateurMapper {
    public static UtilisateurDTO toDto(Utilisateur entity){
        return UtilisateurDTO.builder()
                .id(entity.getId())
                .role(entity.getRole())
                .email(entity.getEmail())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .build();
    }
    public static Utilisateur toEntity(UtilisateurDTO entity){
        return Utilisateur.builder()
                .id(entity.id())
                .role(entity.role())
                .email(entity.email())
                .nom(entity.nom())
                .prenom(entity.prenom())
                .build();
    }
}
