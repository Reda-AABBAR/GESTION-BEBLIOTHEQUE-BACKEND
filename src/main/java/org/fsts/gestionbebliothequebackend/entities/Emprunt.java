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

    public int getJoursRetard() {
        if (dateRetour != null) {
            // Si le document est déjà retourné, aucun retard
            return 0;
        }

        // Convertir `dateEmprunt` en LocalDate pour les calculs
        LocalDate dateEmpruntLocal = dateEmprunt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Définir la limite d'emprunt (3 jours après la date d'emprunt)
        LocalDate dateLimite = dateEmpruntLocal.plusDays(3);

        // Obtenir la date actuelle
        LocalDate dateActuelle = LocalDate.now();

        // Vérifier si la date actuelle dépasse la date limite
        if (dateActuelle.isAfter(dateLimite)) {
            // Calculer et retourner le nombre de jours de retard
            return (int) ChronoUnit.DAYS.between(dateLimite, dateActuelle);
        }

        // Pas de retard
        return 0;


    }
}
