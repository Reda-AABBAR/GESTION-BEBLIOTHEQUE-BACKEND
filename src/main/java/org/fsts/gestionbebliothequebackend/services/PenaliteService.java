package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.entities.Penalite;

import java.util.*;

public interface PenaliteService {
    public Penalite ajouterouUpdatePenalite(Penalite penalite);
    public Optional<Penalite> getPenaliteById(UUID id);
    public List<Penalite> getAllPenalites();
    public void deletePenaliteById(UUID id);
    public List<Penalite> getPenalitesByUtilisateur(UUID utilisateurId);
}
