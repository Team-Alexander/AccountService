package com.uptalent.account.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.text.ParseException;
import java.util.List;

import static com.uptalent.account.jwt.JwtConstant.ROLE_CLAIM;

@Slf4j
public final class JwtParser {
    private JwtParser(){}

    public static Authentication getAuthentication(String token) {
        try {
            JWTClaimsSet claimsSet = parseJwt(token);
            long id = getIdFromSubject(claimsSet);
            GrantedAuthority authority = getAuthority(claimsSet);
            return new UsernamePasswordAuthenticationToken(id, null, List.of(authority));
        } catch (ParseException e) {
            log.error("Error when parsing jwt: ", e);
        }
        return null;
    }

    public static JWTClaimsSet parseJwt(String token) throws ParseException {
            return JWTParser.parse(token).getJWTClaimsSet();
    }

    public static long getIdFromSubject(JWTClaimsSet claimsSet) {
        return Long.parseLong(claimsSet.getSubject());
    }

    public static String getRole(JWTClaimsSet claimsSet) throws ParseException {
        return claimsSet.getStringClaim(ROLE_CLAIM);
    }

    public static GrantedAuthority getAuthority(JWTClaimsSet claimsSet) throws ParseException {
        return new SimpleGrantedAuthority(getRole(claimsSet));
    }
}
