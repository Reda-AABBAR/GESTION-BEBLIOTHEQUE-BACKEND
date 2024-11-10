package org.fsts.gestionbebliothequebackend.repositories;

import org.fsts.gestionbebliothequebackend.dtos.Etudiant;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,UUID> {

    Optional<Utilisateur> findByEmail(String email);
    @Query("SELECT COUNT(u) FROM Utilisateur as u WHERE u.email = :email")
    int countByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) FROM Utilisateur as u")
    int countUtilisateur();


    List<Utilisateur> findByRole(UtilisateurRole role);

    int countByRole(UtilisateurRole role);
    @Query("SELECT COUNT(u) FROM Utilisateur as u WHERE u.code = :code")
    int countByCode(@Param("code") String code);

    Optional<Utilisateur> findById(UUID id);
}
