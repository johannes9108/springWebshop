package springWebshop.application.integration.account;

import org.springframework.data.jpa.repository.JpaRepository;

import springWebshop.application.model.domain.user.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {

    @Override
    default Optional<Customer> findById(Long id){
        return findByIdFullFetch(id);
    }

}
