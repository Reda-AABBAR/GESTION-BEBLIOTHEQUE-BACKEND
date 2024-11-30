package org.fsts.gestionbebliothequebackend.services.impl;

import lombok.RequiredArgsConstructor;
import org.fsts.gestionbebliothequebackend.mappers.UtilisateurMapper;
import org.fsts.gestionbebliothequebackend.services.PhotoService;


import org.fsts.gestionbebliothequebackend.dtos.UtilisateurDTO;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Photo;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.repositories.PhotoRepository;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UtilisateurDTO savePhotoToUser(UUID userID, byte[] photoBytes) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findById(userID);
        if (optionalUtilisateur.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur avec l'ID " + userID + " n'existe pas.");
        }

        Utilisateur utilisateur = optionalUtilisateur.get();
        Photo photo = new Photo();
        photo.setPhoto(photoBytes);
        Photo savedPhoto = photoRepository.save(photo);

        utilisateur.setPhoto(savedPhoto);
        Utilisateur updatedUtilisateur = utilisateurRepository.save(utilisateur);
        return UtilisateurMapper.toDto(updatedUtilisateur);
    }

    @Override
    public Document savePhotoDocument(Long id, byte[] photo) {
        // this method will be implemented by Abdelhaq
        throw new UnsupportedOperationException("Cette méthode n'est pas encore implémentée.");
    }

    @Override
    public Photo savePhoto(byte[] photoBytes) {
        Photo photo = new Photo();
        photo.setPhoto(photoBytes);
        return photoRepository.save(photo);
    }

    @Override
    public Photo getPhotoById(UUID id) {
        return photoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Photo avec l'ID " + id + " n'existe pas.")
        );
    }

    @Override
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    @Override
    public List<Photo> getListPhotosByIds(List<UUID> ids) {
        return photoRepository.findAllById(ids);
    }

    @Override
    public Photo getPhotoByUserId(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow();
        return utilisateur.getPhoto();
    }
}

