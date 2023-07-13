package com.uptalent.account.service;

import com.uptalent.account.exception.NoSuchRoleException;
import com.uptalent.account.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountSecurityService {
    public boolean isPersonalProfile(Long requiredId) {
        Long principalId = getIdFromPrincipal();
        return Objects.equals(requiredId, principalId);
    }

    public Long getIdFromPrincipal() {
        String id = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return (!id.equals("anonymousUser") ? Long.parseLong(id) : 0L);
    }

    public Role getRoleFromAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roleName = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(NoSuchRoleException::new);
        return Role.valueOf(roleName);
    }
}
