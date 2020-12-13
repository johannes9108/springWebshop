package springWebshop.application.integration.account;

import org.springframework.data.jpa.repository.JpaRepository;

import springWebshop.application.model.domain.user.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>{

    Optional<Account> findByEmail(String email);
    Optional<Account> findByUsername(String username);
    boolean existsAccountByEmail(String email);
}
