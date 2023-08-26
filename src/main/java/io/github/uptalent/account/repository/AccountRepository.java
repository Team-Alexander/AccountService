package io.github.uptalent.account.repository;

import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.starter.model.enums.ModerationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @Query(nativeQuery = true, value = "SELECT CASE WHEN t.firstname IS NOT NULL THEN CONCAT(t.firstname, ' ', t.lastname) "
            + "WHEN s.fullname IS NOT NULL THEN s.fullname END AS name"
            + " FROM account a"
            + " LEFT OUTER JOIN talent_account ta ON a.id = ta.account_id"
            + " LEFT OUTER JOIN talent t ON ta.talent_id = t.id"
            + " LEFT OUTER JOIN sponsor_account sa ON a.id = sa.account_id"
            + " LEFT OUTER JOIN sponsor s ON sa.sponsor_id = s.id"
            + " WHERE a.email = :email"
    )
    String findAccountHolderNameByEmail(String email);

    Page<Account> findAllByStatus(ModerationStatus moderationStatus, Pageable pageable);
}
