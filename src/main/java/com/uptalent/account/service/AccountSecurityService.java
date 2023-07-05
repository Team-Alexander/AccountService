package com.uptalent.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountSecurityService {
    public Long getIdFromPrincipal() {
        String id = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return (!id.equals("anonymousUser") ? Long.parseLong(id) : 0L);
    }
}
