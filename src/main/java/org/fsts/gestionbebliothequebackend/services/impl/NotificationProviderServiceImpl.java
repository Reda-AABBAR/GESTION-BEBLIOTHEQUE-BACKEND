package org.fsts.gestionbebliothequebackend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Notification;
import org.fsts.gestionbebliothequebackend.entities.Reservation;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.NotificationType;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.fsts.gestionbebliothequebackend.services.NotificationProviderService;
import org.fsts.gestionbebliothequebackend.services.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationProviderServiceImpl implements NotificationProviderService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;
    @Override
    public Notification alertDocumentReservedToAllBibliocathere(Document document) {
        Notification notification = Notification.builder()
                .message("Document Reserved with id:"+document.getId()) //to be splitted in the front-end
                .type(NotificationType.NEW_RESERVATION)
                .build();
        notification = notificationService.addNotification(notification);
        messagingTemplate.convertAndSendToUser(
                "all-bibliocataire",
                "/notifications",
                notification
        );
        addNotificationToAllUsersWithRole(UtilisateurRole.BIBLIOTHECAIRE,notification);
        return notification;
    }

    @Override
    public Notification alertReservationStatusHasBeenChanged(Reservation reservation) {
        Notification notification = Notification.builder()
                .message("Reservation-Status-changed-with-status:"+reservation.getReservationStatus()+",Reservation-id:"+reservation.getId()) //to be splitted in the front-end
                .type(NotificationType.RESERVATION_STATUS_CHANGED)
                .build();
        notification = notificationService.addNotification(notification);
        messagingTemplate.convertAndSendToUser(
                reservation.getUtilisateur().getId().toString(),
                "/notifications",
                notification
        );
        Utilisateur user = utilisateurRepository.findById(reservation.getUtilisateur().getId()).orElseThrow(
                ()-> {
                    log.error("No user for id: {}",reservation.getUtilisateur().getId());
                    return null;
                }
        );

        if(user != null){
            user.getNotifications().add(notification);
            utilisateurRepository.save(user);
        }
        return notification;
    }


    private void addNotificationToAllUsersWithRole(UtilisateurRole role, Notification notification){
        List<Utilisateur> users = utilisateurRepository.findByRole(role);
        for (Utilisateur user: users) {
            user.getNotifications().add(notification);
        }
        utilisateurRepository.saveAll(users);
    }
}
