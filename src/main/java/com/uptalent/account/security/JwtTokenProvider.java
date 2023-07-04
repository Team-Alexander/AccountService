package com.uptalent.account.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.uptalent.account.security.JwtConstant.ID_CLAIM;
import static com.uptalent.account.security.JwtConstant.ROLE_CLAIM;

@Component
public class JwtTokenProvider {
    public boolean isTokenValid(String token) {
        try{
            DecodedJWT decodedJWT = JWT.decode(token);
            return !isTokenExpired(decodedJWT) &&
                    StringUtils.isNotBlank(decodedJWT.getSubject());
        } catch (JWTDecodeException e) {
            return false;
        }
    }

    /**
     * Get subject(email) from jwt-token
     *
     * @param token JWT-Token
     *
     * @return email
     * */
    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    public Long getId(String token) {
        return JWT.decode(token).getClaim(ID_CLAIM).asLong();
    }

    /**
     * Check if jwt-token is expired
     *
     * @param decodedJWT Decoded-JWT
     *
     * @return is expired token
     * */
    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        Date expiration = decodedJWT.getExpiresAt();
        return expiration.before(new Date());
    }

    /**
     * Get authority(role) from jwt-token
     *
     * @param token JWT-Token
     *
     * @return authority
     * */
    public GrantedAuthority getAuthority(String token) {
        return new SimpleGrantedAuthority(getRoleFromToken(token));
    }


    /**
     * Parse role from jwt-token
     *
     * @param token JWT-Token
     *
     * @return role
     * */
    private String getRoleFromToken(String token) {
        return JWT.decode(token).getClaim(ROLE_CLAIM).asString();
    }

}
