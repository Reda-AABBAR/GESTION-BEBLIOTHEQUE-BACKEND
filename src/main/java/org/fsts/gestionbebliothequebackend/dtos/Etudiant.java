package org.fsts.gestionbebliothequebackend.dtos;

import lombok.Builder;
import org.fsts.gestionbebliothequebackend.entities.Notification;
import org.fsts.gestionbebliothequebackend.entities.Photo;

import java.util.List;
import java.util.UUID;

@Builder
public record Etudiant(
        UUID id,
        String codeMassar,
        String prenom,
        String nom,
        List<Notification> notifications,
        String email, Photo photo
) {
}
