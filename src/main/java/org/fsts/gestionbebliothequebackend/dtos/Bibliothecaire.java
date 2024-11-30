package org.fsts.gestionbebliothequebackend.dtos;

import lombok.Builder;
import org.fsts.gestionbebliothequebackend.entities.Notification;
import org.fsts.gestionbebliothequebackend.entities.Photo;

import java.util.List;
import java.util.UUID;

@Builder
public record Bibliothecaire(
        UUID id,
        String code,
        String prenom,
        String nom,
        String email,
        List<Notification> notifications
) {
}
