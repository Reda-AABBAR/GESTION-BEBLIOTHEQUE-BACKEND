package org.fsts.gestionbebliothequebackend.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Bibliothecaire(
        UUID id,
        String code,
        String prenom,
        String nom,
        String email
) {
}
