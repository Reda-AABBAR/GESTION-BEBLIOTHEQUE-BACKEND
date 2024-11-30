package org.fsts.gestionbebliothequebackend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.Builder;
import org.fsts.gestionbebliothequebackend.entities.Photo;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record UtilisateurDTO(
        UUID id,
        String nom,
        String prenom,
        String code,
        String email,
        UtilisateurRole role, Photo photo
) {

    public static List<UtilisateurDTO> convertToUtilisateurDTOList(Object utilisateursObj) {
        if (utilisateursObj instanceof List) {
            return ((List<Map<String, Object>>) utilisateursObj).stream()
                    .map(UtilisateurDTO::mapToUtilisateurDTO)
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Invalid utilisateurs data format");
    }

    static UtilisateurDTO mapToUtilisateurDTO(Map<String, Object> userMap) {
        return UtilisateurDTO.builder()
                .prenom((String) userMap.get("prenom"))
                .nom((String) userMap.get("nom"))
                .email((String) userMap.get("email"))
                .code((String) userMap.get("code"))
                .role(getUserRole((String) userMap.get("role")))
                .build();
    }
    static UtilisateurRole getUserRole(String role){
        return switch (role) {
            case "BIBLIOTHECAIRE" -> UtilisateurRole.BIBLIOTHECAIRE;
            case "ETUDIANT" -> UtilisateurRole.ETUDIANT;
            case "ADMIN" -> UtilisateurRole.ADMIN;
            case "PERSONNEL" -> UtilisateurRole.PERSONNEL;
            case "RESPONSABLE" -> UtilisateurRole.RESPONSABLE;
            default -> null;
        };
    }
}
