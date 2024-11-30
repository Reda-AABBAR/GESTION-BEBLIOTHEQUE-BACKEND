package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.dtos.UtilisateurDTO;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Photo;

import java.util.List;
import java.util.UUID;

public interface PhotoService {
    UtilisateurDTO savePhotoToUser(UUID userID,byte[] photo);
    Document savePhotoDocument(Long id, byte[] photo);

    Photo savePhoto(byte[] photo);
    Photo getPhotoById(UUID id);
    List<Photo> getAllPhotos();
    List<Photo> getListPhotosByIds(List<UUID> ids);

    Photo getPhotoByUserId(UUID id);
}
