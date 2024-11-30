package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Notification;
import org.fsts.gestionbebliothequebackend.entities.Reservation;

public interface NotificationProviderService {
    Notification alertDocumentReservedToAllBibliocathere(Document document);
    Notification alertReservationStatusHasBeenChanged(Reservation reservation);
}
