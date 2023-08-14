package io.github.uptalent.account.repository;

import io.github.uptalent.account.model.hash.DeletedAccount;
import org.springframework.data.repository.CrudRepository;

public interface DeletedAccountRepository extends CrudRepository<DeletedAccount, String> {
}
