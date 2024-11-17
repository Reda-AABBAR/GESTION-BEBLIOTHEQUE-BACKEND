package org.fsts.gestionbebliothequebackend.config.security;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RsaKeyConfig {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
}