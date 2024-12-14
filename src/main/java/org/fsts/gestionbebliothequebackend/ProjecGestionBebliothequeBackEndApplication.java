package org.fsts.gestionbebliothequebackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fsts.gestionbebliothequebackend.config.security.RsaKeyConfig;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;
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
    public CommandLineRunner commandLineRunner(UtilisateurRepository repository){
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
    @Bean
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
