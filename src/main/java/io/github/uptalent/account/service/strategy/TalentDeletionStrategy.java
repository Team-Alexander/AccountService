package io.github.uptalent.account.service.strategy;

import io.github.uptalent.account.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TalentDeletionStrategy implements AccountDeletionStrategy{
    private final TalentRepository talentRepository;

    @Override
    public void deleteProfile(Long id) {
        talentRepository.deleteById(id);
    }
}
