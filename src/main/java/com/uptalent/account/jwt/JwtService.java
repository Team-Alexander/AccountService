package com.uptalent.account.jwt;

import com.uptalent.account.client.AuthClient;
import com.uptalent.account.converter.PublicKeyConverter;
import com.uptalent.account.model.response.PublicKeyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.uptalent.account.jwt.JwtConstant.ROLE_CLAIM;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final AuthClient authClient;
    private final ConcurrentHashMap<String, PublicKey> jtiMap = new ConcurrentHashMap<>();
    private JwtDecoder jwtDecoder;

    public Jwt decode(String token) {
        updatePublicKey(token);
        return jwtDecoder.decode(token);
    }

    public Authentication getAuthentication(Jwt jwt) {
        long id = getId(jwt);
        GrantedAuthority authority = getAuthority(jwt);
        return new UsernamePasswordAuthenticationToken(id, null, List.of(authority));
    }

    public long getId(Jwt jwt){
        return Long.parseLong(jwt.getSubject());
    }

    public GrantedAuthority getAuthority(Jwt jwt) {
        return new SimpleGrantedAuthority(getRole(jwt));
    }

    private String getRole(Jwt jwt) {
        return jwt.getClaimAsString(ROLE_CLAIM);
    }

    private void updatePublicKey(String token) {
        PublicKey cachedPublicKey = jtiMap.get(getJwtId(token));
        if (cachedPublicKey == null)  {
            try {
                PublicKeyDTO publicKeyDTO = authClient.getPublicKey();
                PublicKey publicKey = PublicKeyConverter.convertToPublicKey(publicKeyDTO);
                configureJwtDecoderWithPublicKey(publicKey);
                jtiMap.put(getJwtId(token), publicKey);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                log.error("Convert to public key failed: ", ex);
            }
        }
    }

    private void configureJwtDecoderWithPublicKey(PublicKey publicKey) {
        this.jwtDecoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey).build();
    }

    private String getJwtId(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getId();
        } catch (NullPointerException | BadJwtException ex) {
            return "";
        }
    }

    @Scheduled(fixedDelay = 2 * 3600 * 1000)
    private void clearJtiMap() {
        log.info("Clearing jti map");
        jtiMap.clear();
    }
}
