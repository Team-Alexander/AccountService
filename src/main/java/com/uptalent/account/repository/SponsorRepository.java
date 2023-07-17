package com.uptalent.account.repository;

import com.uptalent.account.model.entity.Account;
import com.uptalent.account.model.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    Optional<Sponsor> findSponsorByAccount(Account account);
}
