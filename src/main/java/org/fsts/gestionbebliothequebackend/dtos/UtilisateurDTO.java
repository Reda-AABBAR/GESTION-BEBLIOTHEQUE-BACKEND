package org.fsts.gestionbebliothequebackend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.Builder;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;

import java.util.UUID;
@Builder
public record UtilisateurDTO(
        UUID id,
        String nom,
        String prenom,
        String email,
        UtilisateurRole role
) {
}
