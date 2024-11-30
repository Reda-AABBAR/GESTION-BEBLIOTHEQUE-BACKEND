package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.entities.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    Notification addNotification(Notification notification);
    Notification updateNotification(UUID id, Notification notification);
    void deleteNotification(UUID id);
    Notification getNotificationById(UUID id);
    List<Notification> getAll();

}
