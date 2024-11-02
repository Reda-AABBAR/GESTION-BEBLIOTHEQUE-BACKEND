package org.fsts.gestionbebliothequebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;
    protected String nom;
    protected String prenom;
    @Column(unique = true)
    protected String email;
    @JsonIgnore
    protected String password;
    @Column(unique = true)
    protected String code;
    protected UtilisateurRole role;
}
