package org.fsts.gestionbebliothequebackend.dtos;

import lombok.Builder;
import org.fsts.gestionbebliothequebackend.entities.Photo;

import java.util.UUID;

@Builder
public record Personnel(
        UUID id,
        String code,
        String prenom,
        String nom,
        String email, Photo photo
) {
}
