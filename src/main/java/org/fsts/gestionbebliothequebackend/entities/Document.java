package org.fsts.gestionbebliothequebackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Data @NoArgsConstructor @AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> auteurs;

    private String titre;
    private String sousTitre;
    private String edition;
    private String cote1;
    private String cote2;

    @ElementCollection
    @Column(length = 1000)
    private List<String> descripteurs;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EXIST;

    public enum Statut {
        EXIST,
        NOT_EXIST
    }
    @Lob
    private byte[] img;

}
