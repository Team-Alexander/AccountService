package com.uptalent.account.repository;

import com.uptalent.account.model.entity.Account;
import com.uptalent.account.model.entity.Talent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalentRepository extends JpaRepository<Talent, Long> {
    Optional<Talent> findTalentByAccount(Account account);
}
