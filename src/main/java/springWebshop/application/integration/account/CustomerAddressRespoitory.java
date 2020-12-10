package springWebshop.application.integration.account;

import org.springframework.data.jpa.repository.JpaRepository;
import springWebshop.application.model.domain.user.CustomerAddress;

import java.util.List;

public interface CustomerAddressRespoitory extends JpaRepository<CustomerAddress, Long> {

    List<CustomerAddress> findByCustomerId(long customerId);
}
