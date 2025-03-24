package encryption.algorithm.demos.aesgcmmasking.repository;

import encryption.algorithm.demos.aesgcmmasking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findById(Long id);
}
