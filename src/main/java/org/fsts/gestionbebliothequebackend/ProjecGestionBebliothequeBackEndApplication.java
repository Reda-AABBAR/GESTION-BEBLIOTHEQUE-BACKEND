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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //@Bean
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
            "img": "data:image/png;base64,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",
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
