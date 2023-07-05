package com.uptalent.account.jwt;

import com.uptalent.account.client.AuthClient;
import com.uptalent.account.converter.PublicKeyConverter;
import com.uptalent.account.model.response.PublicKeyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    private final AuthClient authClient;

    @Bean
    public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKeyDTO publicKeyDTO = authClient.getPublicKey();
        PublicKey publicKey = PublicKeyConverter.convertToPublicKey(publicKeyDTO);
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey).build();
    }
}
