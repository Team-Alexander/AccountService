package com.uptalent.account.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uptalent.account.model.entity.Account;
import com.uptalent.account.repository.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.uptalent.account.security.JwtConstant.FORBIDDEN_MESSAGE;
import static com.uptalent.account.security.JwtConstant.TOKEN_HEADER;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        /* check if header with jwt-token exists */
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_HEADER)) {

            String jwtToken = authorizationHeader.substring(TOKEN_HEADER.length());

            if(!jwtTokenProvider.isTokenValid(jwtToken)&&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String responseBody = objectMapper.writeValueAsString(Map.of("message", FORBIDDEN_MESSAGE));
                response.getWriter().write(responseBody);
                return;
            }

            String email = jwtTokenProvider.getSubject(jwtToken);
            Long id = jwtTokenProvider.getId(jwtToken);
            Optional<Account> account = accountRepository.findByEmailIgnoreCase((email));

            if (account.isPresent()) {
                GrantedAuthority authority = jwtTokenProvider.getAuthority(jwtToken);
                Authentication authentication = getAuthentication(id, authority, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(Long id, GrantedAuthority authority, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(id, null, List.of(authority));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }
}
