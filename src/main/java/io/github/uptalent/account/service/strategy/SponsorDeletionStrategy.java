package io.github.uptalent.account.service.strategy;

import io.github.uptalent.account.repository.SponsorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SponsorDeletionStrategy implements AccountDeletionStrategy{
    private final SponsorRepository sponsorRepository;

    @Override
    public void deleteProfile(Long id) {
        sponsorRepository.deleteById(id);
    }
}
