package org.fsts.gestionbebliothequebackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fsts.gestionbebliothequebackend.config.security.RsaKeyConfig;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Emprunt;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.repositories.EmpruntRepository;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
import org.fsts.gestionbebliothequebackend.services.DocumentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringBootApplication
@EnableConfigurationProperties({RsaKeyConfig.class})
public class ProjecGestionBebliothequeBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjecGestionBebliothequeBackEndApplication.class, args);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner commandLineRunner(UtilisateurRepository repository, EmpruntRepository empruntRepository, DocumentRepository documentRepository){
        return args ->{
                Utilisateur util = Utilisateur.builder()
                        .code("10001")
                        .email("test@test.com")
                        .nom("test")
                        .prenom("test")
                        .role(UtilisateurRole.ADMIN)
                        .password(passwordEncoder().encode("1234"))
                        .build();
                if(repository.countByEmail("test@test.com")== 0)
                    repository.save(util);
            Utilisateur utilisateur = repository.findByEmail("test@test.com").get();
            Document document = documentRepository.findById(1L).get();

            LocalDate now = LocalDate.now();
            Date dateEmprunt1 = Date.from(now.minusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateEmprunt2 = Date.from(now.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateEmprunt3 = Date.from(now.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateEmprunt4 = Date.from(now.minusDays(4).atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Création des emprunts fictifs en retard
            //Emprunt emprunt1 = new Emprunt(null, utilisateur, document, dateEmprunt1, null, Emprunt.Statut.RETARD);
            Emprunt emprunt2 = new Emprunt(null, utilisateur, document, dateEmprunt2, null, Emprunt.Statut.ATTENTE);
            //Emprunt emprunt3 = new Emprunt(null, utilisateur, document, dateEmprunt3, null, Emprunt.Statut.RETARD);
            //Emprunt emprunt4 = new Emprunt(null, utilisateur, document, dateEmprunt4, null, Emprunt.Statut.RETARD);

            // Sauvegarde dans la base de données
            //empruntRepository.save(emprunt1);
            //empruntRepository.save(emprunt2);
            //empruntRepository.save(emprunt3);
            //empruntRepository.save(emprunt4);

            System.out.println(">>> 4 emprunts en retard ont été ajoutés pour le test.");
        };
    }
    //@Bean
    public CommandLineRunner testSaveDocument(DocumentService documentService) {
        return args -> {
            ObjectMapper objectMapper = new ObjectMapper();

            // Exemple de JSON à sauvegarder
            String jsonString = """
        {
            "titre": "Apprendre Spring Boot",
            "sousTitre": "Une introduction pratique",
            "edition": "1ère édition",
            "cote1": "001.12",
            "cote2": "SPRING-101",
            "nbrExemplaire": 3,
            "auteurs": ["John Doe", "Jane Smith"],
            "descripteurs": ["Spring", "Boot", "Java"],
            "img": "iVBORw0KGgoAAAANSUhEUgAAAAUA",
            "statut": "EXIST"
        }
        """;

            try {
                // Convertir le JSON string en JsonNode
                JsonNode jsonNode = objectMapper.readTree(jsonString);

                // Appeler la méthode saveDocumentFromJson
                Document savedDocument = documentService.saveDocumentFromJson(jsonNode);

                // Afficher le document sauvegardé dans la console
                System.out.println("Document sauvegardé : " + savedDocument);

            } catch (Exception e) {
                // Gérer les exceptions
                System.err.println("Erreur lors de la sauvegarde du document : " + e.getMessage());
            }
        };
    }

}
