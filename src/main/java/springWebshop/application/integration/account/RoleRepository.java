package springWebshop.application.integration.account;

import org.springframework.data.jpa.repository.JpaRepository;
import springWebshop.application.model.domain.user.ERole;
import springWebshop.application.model.domain.user.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
