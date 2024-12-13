package org.fsts.gestionbebliothequebackend.services.impl;

import org.fsts.gestionbebliothequebackend.entities.Penalite;
import org.fsts.gestionbebliothequebackend.repositories.PenaliteRepository;
import org.fsts.gestionbebliothequebackend.services.PenaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PenaliteServiceImpl implements PenaliteService {

    @Autowired
    PenaliteRepository penaliteRepository;

    public Penalite ajouterouUpdatePenalite(Penalite penalite) {
        return penaliteRepository.save(penalite);
    }

    public Optional<Penalite> getPenaliteById(UUID id) {
        return penaliteRepository.findById(id);
    }

    public List<Penalite> getAllPenalites() {
        return penaliteRepository.findAll();
    }

    public void deletePenaliteById(UUID id) {
        penaliteRepository.deleteById(id);
    }

    public List<Penalite> getPenalitesByUtilisateur(UUID utilisateurId) {
        return penaliteRepository.findByUtilisateurId(utilisateurId);
    }

}
