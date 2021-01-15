package springWebshop.application.integration.account;

import org.springframework.data.jpa.repository.JpaRepository;
import springWebshop.application.model.domain.user.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
