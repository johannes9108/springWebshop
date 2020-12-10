package springWebshop.application.integration.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import springWebshop.application.model.domain.user.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	
	@Query("select c from Company c")
	List<Company> test();

	
	
//	@Query("select company from Company company "
//			+ "inner join fetch c.company company "
//			+ "where company.id = :companyIdParameter")
//	Company findCompanyWithLazyCustomersById(@Param("companyIdParameter")long id);

}
