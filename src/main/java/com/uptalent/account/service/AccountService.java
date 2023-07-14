package com.uptalent.account.service;

import com.uptalent.account.model.enums.Role;
import com.uptalent.account.model.request.AuthRegister;
import com.uptalent.account.service.strategy.SponsorDeletionStrategy;
import com.uptalent.account.service.strategy.TalentDeletionStrategy;
import com.uptalent.account.service.visitor.AccountRegisterVisitor;
import com.uptalent.account.service.visitor.AccountUpdateVisitor;
import com.uptalent.account.model.request.AccountUpdate;
import com.uptalent.account.model.response.AccountProfile;
import com.uptalent.account.model.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.uptalent.account.model.enums.Role.SPONSOR;
import static com.uptalent.account.model.enums.Role.TALENT;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountUpdateVisitor accountUpdateVisitor;
    private final AccountRegisterVisitor accountRegisterVisitor;
    private final AccountSecurityService accountSecurityService;
    private final TalentDeletionStrategy talentDeletionStrategy;
    private final SponsorDeletionStrategy sponsorDeletionStrategy;

    public AuthResponse save(AuthRegister authRegister) {
        return authRegister.accept(accountRegisterVisitor);
    }

    public AccountProfile updateProfile(Long id, AccountUpdate accountUpdate){
        return accountUpdate.accept(id, accountUpdateVisitor);
    }

    public void deleteProfile(Long id) {
        Role role = accountSecurityService.getRoleFromAuthorities();

        if (role.equals(TALENT)) {
            talentDeletionStrategy.deleteProfile(id);
        } else if (role.equals(SPONSOR)) {
            sponsorDeletionStrategy.deleteProfile(id);
        }
    }
}
