package org.fsts.gestionbebliothequebackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

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

    // ajouter duree (définie par bibliocather)
    // date départ
}
