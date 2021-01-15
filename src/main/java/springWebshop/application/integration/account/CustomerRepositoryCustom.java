package springWebshop.application.integration.account;

import springWebshop.application.model.domain.user.Customer;

import java.util.Optional;

public interface CustomerRepositoryCustom {

    Optional<Customer> findByIdFullFetch(Long id);
}
