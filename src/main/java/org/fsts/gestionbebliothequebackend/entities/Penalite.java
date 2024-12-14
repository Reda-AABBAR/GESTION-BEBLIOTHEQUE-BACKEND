package org.fsts.gestionbebliothequebackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;
import java.util.Calendar;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Penalite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "dateDebut")
    private Date dateDebut;

    @Column(name = "duree-Penalite")
    private int duree;

    public Date getDateFin() {
        if (dateDebut == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateDebut);
        calendar.add(Calendar.DAY_OF_MONTH, duree);

        return calendar.getTime();
    }

}
