package io.github.uptalent.account.repository;

import io.github.uptalent.account.model.hash.TokenEmail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenEmailRepository extends CrudRepository<TokenEmail, String> {
}
