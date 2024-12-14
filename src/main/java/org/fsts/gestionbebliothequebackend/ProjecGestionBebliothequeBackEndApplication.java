package org.fsts.gestionbebliothequebackend;

import org.fsts.gestionbebliothequebackend.config.security.RsaKeyConfig;
import org.fsts.gestionbebliothequebackend.entities.Utilisateur;
import org.fsts.gestionbebliothequebackend.enums.UtilisateurRole;
import org.fsts.gestionbebliothequebackend.repositories.UtilisateurRepository;
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
                repository.save(util);
        };
    }
}
