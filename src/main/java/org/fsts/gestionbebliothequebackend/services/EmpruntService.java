package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Penalite;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.ReservationStatus;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.repositories.EmpruntRepository;
import org.fsts.gestionbebliothequebackend.repositories.PenaliteRepository;
import org.fsts.gestionbebliothequebackend.repositories.ReservationRepository;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.fsts.gestionbebliothequebackend.services.impl.PenaliteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmpruntService {
    @Autowired
    private final EmpruntRepository empruntRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    PenaliteServiceImpl penaliteServiceImpl;
    private final ReservationRepository reservationRepository;

    public EmpruntService(EmpruntRepository empruntRepository, ReservationRepository reservationRepository) {
        this.empruntRepository = empruntRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Emprunt> getAllEmpruntsByDocument(Document document) {
        return empruntRepository.findByDocument(document);
    }

    public List<Emprunt> getAllEmpruntsByUtilisateur(Utilisateur utilisateur) {
        return empruntRepository.findByUtilisateur(utilisateur);
    }
    public Emprunt saveEmprunt(UUID utilisateurId, Long documentId, Emprunt emprunt) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(utilisateurId);
        Optional<Document> document = documentRepository.findById(documentId);


        if (utilisateur.isPresent() && document.isPresent()) {//cheching if it exist
            int activeEmpruntsCount = empruntRepository.countByDocumentAndDateRetourIsNull(document.get()); // Active loans
            int activeReservationsCount = reservationRepository.countByDocumentAndDateReservationAfterAndReservationStatus(document.get(), new Date(), ReservationStatus.ACCEPTED); // Active reservations
            if ((activeEmpruntsCount + activeReservationsCount) >= document.get().getNbrExemplaire()) {
                throw new IllegalStateException("Document with ID " + documentId + " is not available for loan. All exemplars are currently loaned or reserved.");
            }
            int activeUserEmpruntsCount = empruntRepository.countByUtilisateurAndDateEmprunt(utilisateur.get(), emprunt.getDateEmprunt());
            if (activeUserEmpruntsCount >= 2) {
                throw new IllegalStateException("Utilisateur with ID " + utilisateurId + " already has 2 active loans on " + emprunt.getDateEmprunt());
            }
            if(peutEmprunter(utilisateurId,emprunt.getDateEmprunt(),emprunt.getDateRetour())) {
                emprunt.setUtilisateur(utilisateur.get());
                emprunt.setDocument(document.get());
                return empruntRepository.save(emprunt);
            }else throw new IllegalArgumentException("La date d emprunt est invalid");
        } else {
            throw new IllegalArgumentException("Invalid Document or Utilisateur ID.");
        }
    }

    public void deleteEmprunt(Long empruntId) {
        if (empruntRepository.existsById(empruntId)) {
            empruntRepository.deleteById(empruntId);
        } else {
            throw new IllegalArgumentException("Emprunt with ID " + empruntId + " does not exist.");
        }
    }

    public Emprunt updateEmprunt(Long empruntId, Emprunt updatedEmprunt) {
        Optional<Emprunt> existingEmprunt = empruntRepository.findById(empruntId);

        if (existingEmprunt.isPresent()) {
            Emprunt emprunt = existingEmprunt.get();

            // Get the existing and updated loan dates
            Date oldDateEmprunt = emprunt.getDateEmprunt();
            Date oldDateRetour = emprunt.getDateRetour();
            Date newDateEmprunt = updatedEmprunt.getDateEmprunt();
            Date newDateRetour = updatedEmprunt.getDateRetour();

            // Check if the dates have been changed, and if so, validate them
            boolean datesChanged = !newDateEmprunt.equals(oldDateEmprunt) || !newDateRetour.equals(oldDateRetour);

            if (datesChanged) {
                // Check if the document is available with the new dates
                Optional<Document> document = documentRepository.findById(emprunt.getDocument().getId());
                if (document.isPresent()) {
                    int activeEmpruntsCount = empruntRepository.countByDocumentAndDateRetourIsNull(document.get()); // Active loans
                    int activeReservationsCount = reservationRepository.countByDocumentAndDateReservationAfterAndReservationStatus(
                            document.get(), new Date(), ReservationStatus.ACCEPTED); // Active reservations

                    if ((activeEmpruntsCount + activeReservationsCount) >= document.get().getNbrExemplaire()) {
                        throw new IllegalStateException("Document with ID " + emprunt.getDocument().getId() + " is not available for loan. All exemplars are currently loaned or reserved.");
                    }
                } else {
                    throw new IllegalArgumentException("Document with ID " + emprunt.getDocument().getId() + " does not exist.");
                }

                // Check if the user exceeds the limit of active loans for the new loan date
                int activeUserEmpruntsCount = empruntRepository.countByUtilisateurAndDateEmprunt(updatedEmprunt.getUtilisateur(), newDateEmprunt);
                if (activeUserEmpruntsCount >= 2) {
                    throw new IllegalStateException("Utilisateur with ID " + updatedEmprunt.getUtilisateur().getId() + " already has 2 active loans on " + newDateEmprunt);
                }

                // Validate the new dates (you can add your specific logic in `peutEmprunter` method)
                if (!peutEmprunter(updatedEmprunt.getUtilisateur().getId(), newDateEmprunt, newDateRetour)) {
                    throw new IllegalArgumentException("La date d'emprunt est invalide.");
                }

                // Update the emprunt object with the new values
                emprunt.setDateEmprunt(newDateEmprunt);
                emprunt.setDateRetour(newDateRetour);
            }

            // Update the status if it has changed
            if (updatedEmprunt.getStatut() != null && !updatedEmprunt.getStatut().equals(emprunt.getStatut())) {
                emprunt.setStatut(updatedEmprunt.getStatut());
            }

            // Save the updated emprunt
            return empruntRepository.save(emprunt);
        } else {
            throw new IllegalArgumentException("Emprunt with ID " + empruntId + " does not exist.");
        }
    }

    public List<Emprunt> getEmpruntsRetard() {
        Date currentDate = new Date();
        return empruntRepository.findByDocumentStatutAndDateRetourBefore(Document.Statut.NOT_EXIST, currentDate);
    }
    public List<Emprunt> getEmpruntsRetourner() {
        return empruntRepository.findByDocumentStatut(Document.Statut.EXIST);
    }

    public List<Emprunt> getEmpruntsWithDelay() {

        List<Emprunt> allEmprunts = empruntRepository.findByDocumentStatut(Document.Statut.EXIST);

        List<Emprunt> overdueEmprunts = new ArrayList<>();

        for (Emprunt emprunt : allEmprunts) {
            if (emprunt.getDateRetour() != null) {
                // calculer la date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(emprunt.getDateRetour());
                calendar.add(Calendar.DAY_OF_MONTH, 3); // ajouter 3 jours
                Date dateLimite = calendar.getTime();

                if (emprunt.getDateRetour().after(dateLimite)) {
                    overdueEmprunts.add(emprunt);
                }
            }
        }
        return overdueEmprunts;
    }

    public List<Emprunt> getEmpruntsWithoutDelay(){
        List<Emprunt> allEmprunts = empruntRepository.findByDocumentStatut(Document.Statut.EXIST);

        List<Emprunt> notoverdueEmprunts = new ArrayList<>();

        for (Emprunt emprunt : allEmprunts) {
            if (emprunt.getDateRetour() != null) {
                // calculer la date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(emprunt.getDateRetour());
                calendar.add(Calendar.DAY_OF_MONTH, 3); // ajouter 3 jours
                Date dateLimite = calendar.getTime();

                if (dateLimite.after(emprunt.getDateRetour())) {
                    notoverdueEmprunts.add(emprunt);
                }
            }
        }
        return notoverdueEmprunts;
    }

    public Emprunt updateStatut(Long id, Emprunt.Statut nouveauStatut) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable avec l'ID : " + id));
        emprunt.setStatut(nouveauStatut);
        return empruntRepository.save(emprunt);
    }

    public List<Emprunt> getByStatut(Emprunt.Statut statut) {
        return empruntRepository.findByStatut(statut);
    }

    public List<Emprunt> getAllEmprunts() {
        return empruntRepository.findAll();
    }

    public boolean isEmpruntCountValid(UUID utilisateurId) {
        int empruntCount = empruntRepository.countByUtilisateurId(utilisateurId);
        return empruntCount <= 2;
    }

    public Long getEmpruntsCountThisMonth() {
        return empruntRepository.countEmpruntsByMonth(new Date());
    }

    public List<Emprunt> getEmpruntsEnCours() {
        return empruntRepository.findEmpruntsEnCours();
    }
    public  List<Emprunt> getEmpruntRetournerThisMonth(){
        return empruntRepository.findEmpruntRetournerThisMonth();
    }

    public Map<String, Long> getStatistiquesLivres() {
        Long nbEmpruntes = empruntRepository.countEmpruntsActuels();
        Long nbDisponibles = documentRepository.countDocumentsDisponibles();
        return Map.of(
                "nbEmpruntes", nbEmpruntes,
                "nbDisponibles", nbDisponibles
        );
    }

    public List<Map<String, Object>> getTop30DocumentsEmpruntes() {
        List<Object[]> results = empruntRepository.findTopDocumentsEmpruntes(PageRequest.of(0, 30));
        return results.stream()
                .map(result -> Map.of(
                        "document", (Document) result[0],
                        "empruntCount", (Long) result[1]
                ))
                .collect(Collectors.toList());
    }

    private boolean peutEmprunter(UUID utilisateur_Id, Date dateDebutEmprunt, Date dateFinEmprunt) {
        List<Penalite> penalites = penaliteServiceImpl.getPenalitesByUtilisateur(utilisateur_Id);

        for (Penalite penalite : penalites) {
            Date dateFinPenalite = penalite.getDateFin();

            if ((dateDebutEmprunt.before(dateFinPenalite) && dateDebutEmprunt.after(penalite.getDateDebut())) ||
                    (dateFinEmprunt.after(penalite.getDateDebut()) && dateFinEmprunt.before(dateFinPenalite)) ||
                    (dateDebutEmprunt.before(penalite.getDateDebut()) && dateFinEmprunt.after(dateFinPenalite))) {
                return false;
            }
        }
        return true;
    }

    public Float DureeMoyenDeRetard(){
        List<Emprunt> empruntsretarde = getEmpruntsWithDelay();
        int joursretarstotal = 0;
        for(Emprunt emprunt : empruntsretarde){
            joursretarstotal += emprunt.getJoursRetard();
        }
        Float moyen = (joursretarstotal * 1.0f) / empruntsretarde.size();
        return moyen;
    }
}