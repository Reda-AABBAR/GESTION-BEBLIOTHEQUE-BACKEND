package org.fsts.gestionbebliothequebackend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fsts.gestionbebliothequebackend.dtos.*;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;
import org.fsts.gestionbebliothequebackend.mappers.UtilisateurMapper;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.fsts.gestionbebliothequebackend.services.UtilisateurService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public UtilisateurDTO upDatePassword(UUID id,String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            log.error("Password is too short. It must be at least 6 characters.");
            throw new RuntimeException("Password must be at least 6 characters long.");
        }
        Utilisateur u = repository.findById(id).orElseThrow(
                ()->{
                    log.error("No user for id: {}", id);
                    return null;
                }
        );
        if(u != null){
            u.setPassword(passwordEncoder.encode(newPassword));
            u = repository.save(u);
            return UtilisateurMapper.toDto(u);
        }
        return null;
    }

    @Override
    public UtilisateurDTO saveUtilisateur(UtilisateurDTO dto, String password) {
        if (!dto.email().matches("^[a-zA-Z0-9._%+-]+@uhp\\.ac\\.ma$")) {
            log.error("Email {} is not valid. It should follow the pattern: reda.aabbar@uhp.ac.ma", dto.email());
            throw new RuntimeException("Invalid email format. It should follow the pattern: reda.aabbar@uhp.ac.ma");
        }
        // Validate password length
        if (password == null || password.length() < 6) {
            log.error("Password is too short. It must be at least 6 characters.");
            throw new RuntimeException("Password must be at least 6 characters long.");
        }

        if(isEmailExist(dto.email())){
            log.error("email {} est déjà exist dans la base de données",dto.email());
            throw new RuntimeException("email déjà exist dans la base de données");
        }
        if(iscodeExist(dto.code())){
            log.error("code {} est déjà exist dans la base de données",dto.code());
            throw new RuntimeException("le code déjà exist dans la base de données");
        }
        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);
        utilisateur.setPassword(passwordEncoder.encode(password));
        Utilisateur save = repository.save(utilisateur);
        return UtilisateurMapper.toDto(save);
    }

    @Override
    public UtilisateurDTO updateUtilisateur(UUID id, UtilisateurDTO dto) {
        if (!dto.email().matches("^[a-zA-Z0-9._%+-]+@uhp\\.ac\\.ma$")) {
            log.error("Email {} is not valid. It should follow the pattern: reda.aabbar@uhp.ac.ma", dto.email());
            throw new RuntimeException("Invalid email format. It should follow the pattern: reda.aabbar@uhp.ac.ma");
        }
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
            utilisateur.setCode(dto.code());
            return UtilisateurMapper.toDto(repository.save(utilisateur));
        }
        return null;
    }

    @Override
    public UtilisateurDTO updateUtilisateurByEmail(String email, UtilisateurDTO dto) {
        if (!dto.email().matches("^[a-zA-Z0-9._%+-]+@uhp\\.ac\\.ma$")) {
            log.error("Email {} is not valid. It should follow the pattern: reda.aabbar@uhp.ac.ma", dto.email());
            throw new RuntimeException("Invalid email format. It should follow the pattern: reda.aabbar@uhp.ac.ma");
        }
        Utilisateur utilisateur = repository.findByEmail(email).orElseThrow(
                ()->{
                    log.error("no utilisateur trouver pour l'email {}",email);
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
            utilisateur.setCode(dto.code());
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
    public Etudiant saveEtudiant(Etudiant dto, String password) {
        UtilisateurDTO utilisateur = UtilisateurMapper.EtudiantToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToEtudiant(
                saveUtilisateur(utilisateur,password)
        );
    }

    @Override
    public Etudiant updateUtilisateur(UUID id, Etudiant dto) {
        UtilisateurDTO utilisateur = UtilisateurMapper.EtudiantToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToEtudiant(
                updateUtilisateur(id,utilisateur)
        );
    }

    @Override
    public Etudiant updateEtudiantByEmail(String email, Etudiant dto) {
        UtilisateurDTO utilisateur = UtilisateurMapper.EtudiantToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToEtudiant(
                updateUtilisateurByEmail(email,utilisateur)
        );
    }

    @Override
    @Transactional
    public List<Etudiant> saveAllEtudiant(List<Etudiant> dto, Map<String, String> passwords) {
        List<UtilisateurDTO> utilisateurDTOS = dto.stream().map(UtilisateurMapper::EtudiantToUtilisateurDTO).toList();
        return saveAllUtilisateur(utilisateurDTOS,passwords).stream().map(UtilisateurMapper::UtilisateurToEtudiant).toList();
    }

    @Override
    public Etudiant getEtudiantByEmail(String email) {
        UtilisateurDTO utilisateurDTO = getUtilisateurByEmail(email);
        return (utilisateurDTO.role() != UtilisateurRole.ETUDIANT)? null: UtilisateurMapper.UtilisateurToEtudiant(utilisateurDTO);
    }

    @Override
    public Etudiant getEtudiantById(UUID id) {
        UtilisateurDTO utilisateurDTO = getUtilisateurById(id);
        return (utilisateurDTO.role() != UtilisateurRole.ETUDIANT)? null: UtilisateurMapper.UtilisateurToEtudiant(utilisateurDTO);
    }

    @Override
    public void deleteEtudiantId(UUID id) {
        deleteUtilisateurId(id);
    }

    @Override
    public void deleteListEtudiant(List<UUID> ids) {
        ids.forEach(this::deleteUtilisateurId);
    }

    @Override
    public List<Etudiant> getAllEtudiant() {
        return repository.findByRole(UtilisateurRole.ETUDIANT).stream().map(
                utilisateur -> UtilisateurMapper.UtilisateurToEtudiant(UtilisateurMapper.toDto(utilisateur))
        ).toList();
    }

    @Override
    public Bibliothecaire saveBibliothecaire(Bibliothecaire dto, String password) {
        UtilisateurDTO utilisateur = UtilisateurMapper.BibliothecaireToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToBibliothecaire(
                saveUtilisateur(utilisateur,password)
        );
    }

    @Override
    public Bibliothecaire updateBibliothecaire(UUID id, Bibliothecaire dto) {
        UtilisateurDTO utilisateur = UtilisateurMapper.BibliothecaireToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToBibliothecaire(
                updateUtilisateur(id,utilisateur)
        );
    }

    @Override
    public Bibliothecaire updateBibliothecaireByEmail(String email, Bibliothecaire dto) {
        UtilisateurDTO utilisateur = UtilisateurMapper.BibliothecaireToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToBibliothecaire(
                updateUtilisateurByEmail(email,utilisateur)
        );
    }

    @Override
    public List<Bibliothecaire> saveAllBibliothecaire(List<Bibliothecaire> dto, Map<String, String> passwords) {
        List<UtilisateurDTO> utilisateurDTOS = dto.stream().map(UtilisateurMapper::BibliothecaireToUtilisateurDTO).toList();
        return saveAllUtilisateur(utilisateurDTOS,passwords).stream().map(UtilisateurMapper::UtilisateurToBibliothecaire).toList();
    }

    @Override
    public Bibliothecaire getBibliothecaireByEmail(String email) {
        UtilisateurDTO utilisateurDTO = getUtilisateurByEmail(email);
        return (utilisateurDTO.role() != UtilisateurRole.BIBLIOTHECAIRE)? null: UtilisateurMapper.UtilisateurToBibliothecaire(utilisateurDTO);
    }

    @Override
    public Bibliothecaire getBibliothecaireById(UUID id) {
        UtilisateurDTO utilisateurDTO = getUtilisateurById(id);
        return (utilisateurDTO.role() != UtilisateurRole.BIBLIOTHECAIRE)? null: UtilisateurMapper.UtilisateurToBibliothecaire(utilisateurDTO);
    }

    @Override
    public void deleteBibliothecaireId(UUID id) {
        deleteUtilisateurId(id);
    }

    @Override
    public void deleteListBibliothecaire(List<UUID> ids) {
        ids.forEach(this::deleteUtilisateurId);
    }

    @Override
    public List<Bibliothecaire> getAllBibliothecaire() {
        return repository.findByRole(UtilisateurRole.BIBLIOTHECAIRE).stream().map(
                utilisateur -> UtilisateurMapper.UtilisateurToBibliothecaire(UtilisateurMapper.toDto(utilisateur))
        ).toList();
    }

    @Override
    public Admin saveAdmin(Admin dto, String password) {
        UtilisateurDTO utilisateur = UtilisateurMapper.AdminToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToAdmin(
                saveUtilisateur(utilisateur,password)
        );
    }

    @Override
    public Admin updateAdmin(UUID id, Admin dto) {
        UtilisateurDTO utilisateur = UtilisateurMapper.AdminToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToAdmin(
                updateUtilisateur(id,utilisateur)
        );
    }

    @Override
    public Admin updateAdminByEmail(String email, Admin dto) {
        UtilisateurDTO utilisateur = UtilisateurMapper.AdminToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToAdmin(
                updateUtilisateurByEmail(email,utilisateur)
        );
    }

    @Override
    public List<Admin> saveAllAdmin(List<Admin> dto, Map<String, String> passwords) {
        List<UtilisateurDTO> utilisateurDTOS = dto.stream().map(UtilisateurMapper::AdminToUtilisateurDTO).toList();
        return saveAllUtilisateur(utilisateurDTOS,passwords).stream().map(UtilisateurMapper::UtilisateurToAdmin).toList();
    }

    @Override
    public Admin getAdminByEmail(String email) {
        UtilisateurDTO utilisateurDTO = getUtilisateurByEmail(email);
        return (utilisateurDTO.role() != UtilisateurRole.ADMIN)? null: UtilisateurMapper.UtilisateurToAdmin(utilisateurDTO);
    }

    @Override
    public Admin getAdminById(UUID id) {
        UtilisateurDTO utilisateurDTO = getUtilisateurById(id);
        return (utilisateurDTO.role() != UtilisateurRole.ADMIN)? null: UtilisateurMapper.UtilisateurToAdmin(utilisateurDTO);
    }

    @Override
    public void deleteAdminId(UUID id) {
        deleteUtilisateurId(id);
    }

    @Override
    public void deleteAdmin(List<UUID> ids) {
        ids.forEach(this::deleteUtilisateurId);
    }

    @Override
    public List<Admin> getAllAdmin() {
        return repository.findByRole(UtilisateurRole.ADMIN).stream().map(
                utilisateur -> UtilisateurMapper.UtilisateurToAdmin(UtilisateurMapper.toDto(utilisateur))
        ).toList();
    }

    @Override
    public Personnel savePersonnel(Personnel dto, String password) {
        UtilisateurDTO utilisateur = UtilisateurMapper.PersonnelToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToPersonnel(
                saveUtilisateur(utilisateur,password)
        );
    }

    @Override
    public Personnel updatePersonnel(UUID id, Personnel dto) {
        UtilisateurDTO utilisateur = UtilisateurMapper.PersonnelToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToPersonnel(
                updateUtilisateur(id,utilisateur)
        );
    }

    @Override
    public Personnel updatePersonnelByEmail(String email, Personnel dto) {
        UtilisateurDTO utilisateur = UtilisateurMapper.PersonnelToUtilisateurDTO(dto);
        return UtilisateurMapper.UtilisateurToPersonnel(
                updateUtilisateurByEmail(email,utilisateur)
        );
    }

    @Override
    public List<Personnel> saveAllPersonnel(List<Personnel> dto, Map<String, String> passwords) {
        List<UtilisateurDTO> utilisateurDTOS = dto.stream().map(UtilisateurMapper::PersonnelToUtilisateurDTO).toList();
        return saveAllUtilisateur(utilisateurDTOS,passwords).stream().map(UtilisateurMapper::UtilisateurToPersonnel).toList();
    }

    @Override
    public Personnel getPersonnelByEmail(String email) {
        UtilisateurDTO utilisateurDTO = getUtilisateurByEmail(email);
        return (utilisateurDTO.role() != UtilisateurRole.PERSONNEL)? null: UtilisateurMapper.UtilisateurToPersonnel(utilisateurDTO);
    }

    @Override
    public Personnel getPersonnelById(UUID id) {
        UtilisateurDTO utilisateurDTO = getUtilisateurById(id);
        return (utilisateurDTO.role() != UtilisateurRole.PERSONNEL)? null: UtilisateurMapper.UtilisateurToPersonnel(utilisateurDTO);
    }

    @Override
    public void deletePersonnelId(UUID id) {
        deleteUtilisateurId(id);
    }

    @Override
    public void deletePersonnel(List<UUID> ids) {
        ids.forEach(this::deleteUtilisateurId);
    }

    @Override
    public List<Personnel> getAllPersonnel() {
        return repository.findByRole(UtilisateurRole.PERSONNEL).stream().map(
                utilisateur -> UtilisateurMapper.UtilisateurToPersonnel(UtilisateurMapper.toDto(utilisateur))
        ).toList();
    }

    @Override
    public void deleteUtilisateurId(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<UtilisateurDTO> getAllUtilisateur() {
        return repository.findAll().stream().map(UtilisateurMapper::toDto).toList();
    }

    @Override
    public boolean isEmailExist(String email) {
        return repository.countByEmail(email) != 0;
    }
    boolean iscodeExist(String code){ return repository.countByCode(code) != 0;}

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

    @Override
    public void deleteListUtilisateur(List<UUID> listOfId) {
        listOfId.forEach(this::deleteUtilisateurId);
    }

    @Override
    public void deleteListPersonnel(List<UUID> listOfId) {
        listOfId.forEach(this::deleteUtilisateurId);
    }

    @Override
    public void deleteListAdmin(List<UUID> listOfId) {
        listOfId.forEach(this::deleteUtilisateurId);
    }

    @Override
    public UtilisateurDTO upDatePasswordByEmail(String email, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            log.error("Password is too short. It must be at least 6 characters.");
            throw new RuntimeException("Password must be at least 6 characters long.");
        }
        Utilisateur u = repository.findByEmail(email).orElseThrow(
                ()->{
                    log.error("No user for email: {}", email);
                    return null;
                }
        );
        if(u != null){
            u.setPassword(passwordEncoder.encode(newPassword));
            u = repository.save(u);
            return UtilisateurMapper.toDto(u);
        }
        return null;
    }
}
