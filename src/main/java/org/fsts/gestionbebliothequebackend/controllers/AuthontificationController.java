package org.fsts.gestionbebliothequebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.fsts.gestionbebliothequebackend.dtos.Login;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthontificationController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Login userRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.email(), userRequest.password())
            );

            Instant now = Instant.now();
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                    .issuedAt(now)
                    .expiresAt(now.plus(60, ChronoUnit.MINUTES))
                    .subject(userRequest.email())
                    .claim("scope", scope)
                    .build();

            JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.RS256).build(),  // Use RS256 for RSA keys
                    claimsSet
            );

            String jwt = jwtEncoder.encode(encoderParameters).getTokenValue();
            return Map.of("access-token", jwt);

        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password", e);
        }
    }
}
