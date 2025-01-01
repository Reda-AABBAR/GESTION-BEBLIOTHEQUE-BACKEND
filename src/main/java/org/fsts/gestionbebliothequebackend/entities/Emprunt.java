package org.fsts.gestionbebliothequebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public int getJoursRetard() {


        // Convertir `dateEmprunt` en LocalDate pour les calculs
        LocalDate dateEmpruntLocal = dateEmprunt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (dateRetour == null) {
            LocalDate now = LocalDate.now();
            // Si l'empint n'est pas retourner on va voir s'il est en retard ou pas
            return (dateEmpruntLocal.plusDays(3).isAfter(now))?
                    (int) ChronoUnit.DAYS.between(now,dateEmpruntLocal.plusDays(3)) : 0;
        }
        LocalDate dateRetourLocal = dateRetour.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Définir la limite d'emprunt (3 jours après la date d'emprunt)
        LocalDate dateLimite = dateEmpruntLocal.plusDays(3);

        // Vérifier si la date actuelle dépasse la date limite
        if (dateRetourLocal.isAfter(dateLimite)) {
            // Calculer et retourner le nombre de jours de retard
            return (int) ChronoUnit.DAYS.between(dateLimite, dateRetourLocal.plusDays(3));
        }

        // Pas de retard
        return 0;


    }
}
