package org.fsts.gestionbebliothequebackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Data @NoArgsConstructor @AllArgsConstructor
public class Document {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String auteur;
    private String titre;
    private String sousTitre;
    private String edition;
    private String cote1;
    private String cote2;
    private String descripteurs;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EXIST ;

    public enum Statut {
        EXIST,
        NOT_EXIST
    }
}
