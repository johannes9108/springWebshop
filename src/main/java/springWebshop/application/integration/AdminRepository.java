package springWebshop.application.integration;

import org.springframework.data.jpa.repository.JpaRepository;
import springWebshop.application.model.domain.user.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
