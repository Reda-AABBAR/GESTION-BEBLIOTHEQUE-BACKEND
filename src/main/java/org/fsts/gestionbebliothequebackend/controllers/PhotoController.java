package org.fsts.gestionbebliothequebackend.controllers;


import lombok.RequiredArgsConstructor;
import org.fsts.gestionbebliothequebackend.dtos.UtilisateurDTO;
import org.fsts.gestionbebliothequebackend.entities.Photo;
import org.fsts.gestionbebliothequebackend.services.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    /**
     * Save a photo and associate it with a user.
     *
     * @param userId The ID of the user.
     * @param file   The photo file to save.
     * @return The updated user DTO with the photo.
     */
    @PostMapping("/users/{userId}")
    public ResponseEntity<UtilisateurDTO> savePhotoToUser(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            byte[] photoBytes = file.getBytes();
            UtilisateurDTO utilisateurDTO = photoService.savePhotoToUser(userId, photoBytes);
            return ResponseEntity.ok(utilisateurDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Save a photo to the database.
     *
     * @param file The photo file to save.
     * @return The saved photo.
     */
    @PostMapping
    public ResponseEntity<Photo> savePhoto(@RequestParam("file") MultipartFile file) {
        try {
            byte[] photoBytes = file.getBytes();
            Photo savedPhoto = photoService.savePhoto(photoBytes);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPhoto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Get a photo by its ID.
     *
     * @param id The ID of the photo.
     * @return The photo.
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getPhotoById(@PathVariable UUID id) {
        try {
            Photo photo = photoService.getPhotoById(id);
            return ResponseEntity.ok(photo.getPhoto().toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Get all photos from the database.
     *
     * @return The list of all photos.
     */
    @GetMapping
    public ResponseEntity<List<Photo>> getAllPhotos() {
        List<Photo> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }

    /**
     * Get a list of photos by their IDs.
     *
     * @param ids The list of IDs.
     * @return The list of photos.
     */
    @PostMapping("/by-ids")
    public ResponseEntity<List<Photo>> getListPhotosByIds(@RequestBody List<UUID> ids) {
        List<Photo> photos = photoService.getListPhotosByIds(ids);
        return ResponseEntity.ok(photos);
    }
}

