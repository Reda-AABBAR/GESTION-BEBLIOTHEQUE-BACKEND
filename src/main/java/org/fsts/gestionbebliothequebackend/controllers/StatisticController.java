package org.fsts.gestionbebliothequebackend.controllers;

import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.services.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticController {

    @Autowired
    private EmpruntService empruntService;

    //Nombre de livre emprunter ce mois
    @GetMapping("/emprunts/count-this-month")
    public Long getEmpruntsCountThisMonth() {
        return empruntService.getEmpruntsCountThisMonth();
    }

    //Nombre d emprunt en court
    @GetMapping("/emprunts/en-cours")
    public List<Emprunt> getEmpruntsEnCours() {
        return empruntService.getEmpruntsEnCours();
    }

    //documents disponible : nbre doc emprunter et nbre doc disponible
    @GetMapping("/documents/documents_disponible")
    public Map<String, Long> getStatistiquesLivres() {
        return empruntService.getStatistiquesLivres();
    }

    //top 30 document emprunter : qui sont demander beaucoups
    @GetMapping("/documents/top-documents")
    public List<Map<String, Object>> getTop30DocumentsEmpruntes() {
        return empruntService.getTop30DocumentsEmpruntes();
    }

    //Retour de livre : Nombre d emprunt retourner ce mois
    @GetMapping("/emprunts/retourner_ce_mois")
    public List<Emprunt> getEmpruntsReturnedThisMonth() {
        return empruntService.getEmpruntRetournerThisMonth();
    }

    //jours moyen de retard
    @GetMapping("/emprunts/mpoyen_retard")
    public Float MoyenJoursRetard(){
        return empruntService.DureeMoyenDeRetard();
    }
}
