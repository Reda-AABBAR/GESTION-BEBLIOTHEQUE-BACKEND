package org.fsts.gestionbebliothequebackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", referencedColumnName = "id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private Document document;

    @Column(name = "date_emprunt")
    private Date dateEmprunt;

    @Column(name = "date_retour")
    private Date dateRetour;

    @Enumerated(EnumType.STRING)
    private Emprunt.Statut statut = Statut.ATTENTE;

    public enum Statut {
        RETOURNER,
        ATTENTE,
        RETARD
    }

}
