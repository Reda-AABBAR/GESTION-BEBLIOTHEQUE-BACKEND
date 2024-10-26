package org.fsts.gestionbebliothequebackend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsts.gestionbebliothequebackend.dtos.UtilisateurDTO;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;
import org.fsts.gestionbebliothequebackend.mappers.UtilisateurMapper;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.fsts.gestionbebliothequebackend.services.UtilisateurService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository repository;
    @Override
    public UtilisateurDTO saveUtilisateur(UtilisateurDTO dto, String password) {
        if(isEmailExist(dto.email())){
            log.error("email {} est déjà exist dans la base de données",dto.email());
            throw new RuntimeException("email déjà exist dans la base de données");
        }
        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);
        utilisateur.setPassword(password); // to be encoded
        Utilisateur save = repository.save(utilisateur);
        return UtilisateurMapper.toDto(save);
    }

    @Override
    public UtilisateurDTO updateUtilisateur(UUID id, UtilisateurDTO dto) {
        Utilisateur utilisateur = repository.findById(id).orElseThrow(
                ()->{
                    log.error("no utilisateur trouver pour l'id {}",id);
                    return null;
                }
        );
        if(utilisateur != null){
            if (!utilisateur.getEmail().equals(dto.email()) && isEmailExist(dto.email())){
                log.error("email ({}) déjà exist dans la base de données",dto.email());
                throw new RuntimeException("email déjà exist dans la base de données");
            }
            utilisateur.setEmail(dto.email());
            utilisateur.setNom(dto.nom());
            utilisateur.setPrenom(dto.prenom());
            utilisateur.setRole(dto.role());
            return UtilisateurMapper.toDto(repository.save(utilisateur));
        }
        return null;
    }

    @Transactional
    @Override
    public List<UtilisateurDTO> saveAllUtilisateur(List<UtilisateurDTO> dtos, Map<String,String> passwords) {
        return dtos.stream().map(user -> saveUtilisateur(user,passwords.get(user.email()))).collect(Collectors.toList());
    }

    @Override
    public UtilisateurDTO getUtilisateurByEmail(String email) {
        return UtilisateurMapper.toDto(
                repository.findByEmail(email).orElseThrow(
                        ()->{
                            log.error("il n'y a pas d'utilisateur pour l'email {}",email);
                            return null;
                        }
                )
        );
    }

    @Override
    public UtilisateurDTO getUtilisateurById(UUID id) {
        return UtilisateurMapper.toDto(
                repository.findById(id).orElseThrow(
                        ()->{
                            log.error("il n'y a pas d'utilisateur pour l'id {}",id);
                            return null;
                        }
                )
        );
    }

    @Override
    public void deleteUtilisateurId(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean isEmailExist(String email) {
        return repository.countByEmail(email) != 0;
    }

    @Override
    public int numberUtilisateur() {
        return repository.countUtilisateur();
    }

    @Override
    public int numberEtudiants() {
        return repository.countByRole(UtilisateurRole.ETUDIANT);
    }

    @Override
    public int numberPersonnels() {
        return repository.countByRole(UtilisateurRole.PERSONNEL);
    }

    @Override
    public int numberAdmins() {
        return repository.countByRole(UtilisateurRole.ADMIN);
    }

    @Override
    public int numberResponsables() {
        return repository.countByRole(UtilisateurRole.RESPONSABLE);
    }

    @Override
    public int numberBibliothecaire() {
        return repository.countByRole(UtilisateurRole.BIBLIOTHECAIRE);
    }
}
