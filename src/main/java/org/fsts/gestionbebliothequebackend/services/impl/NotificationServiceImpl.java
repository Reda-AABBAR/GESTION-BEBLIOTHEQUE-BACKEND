package org.fsts.gestionbebliothequebackend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsts.gestionbebliothequebackend.entities.Notification;
import org.fsts.gestionbebliothequebackend.repositories.NotificationRepository;
import org.fsts.gestionbebliothequebackend.services.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    @Override
    public Notification addNotification(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public Notification updateNotification(UUID id, Notification notification) {
        Notification notification1 = repository.findById(id).orElseThrow(
                ()->{
                    log.error("Notification not exist for id {}",id);
                    return null;
                }
        );
        if (notification1 == null){
            return null;
        }

        notification1.setMessage(notification.getMessage());
        notification1.setType(notification.getType());
        return repository.save(notification1);
    }

    @Override
    public void deleteNotification(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Notification getNotificationById(UUID id) {
        return repository.findById(id).orElseThrow(
                ()->{
                    log.error("Notification not exist for id {}",id);
                    return null;
                }
        );
    }

    @Override
    public List<Notification> getAll() {
        return repository.findAll();
    }
}
