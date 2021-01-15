package springWebshop.application.integration.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import springWebshop.application.model.domain.user.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
