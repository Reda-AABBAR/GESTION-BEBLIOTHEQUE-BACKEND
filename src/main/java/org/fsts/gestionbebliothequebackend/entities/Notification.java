package org.fsts.gestionbebliothequebackend.entities;


import jakarta.persistence.*;
import lombok.*;
import org.fsts.gestionbebliothequebackend.enums.NotificationType;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String message;
    private NotificationType type;

    private Date date;

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = new Date();
        }
    }
}
