package springWebshop.application.integration.account;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springWebshop.application.model.domain.order.Address;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.CustomerAddress;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {

    @Override
    default Optional<Customer> findById(Long id){
        return findByIdFullFetch(id);
    }



//    Optional<Customer> findById(Long id){
//        Criteria criteria = session.createCriteria(Customer.class);
//        criteria.setFetchMode("roles", FetchMode.EAGER);
//    }
    //List<CustomerAddress> findCustomerAddressesByCustomer();
//    @Query(value = "select distinct c " +
//            "from Customer c " +
//            "left join fetch c.addresses " +
//            "where c.id = :customerId")
//    Customer findFullCustomerFirstPart(@Param("customerId")long id);

//    @Query(value = "select distinct c " +
//            "from Customer c " +
//            "left join fetch c.addresses " +
//            "where c.id = :customerId;")
//    Optional<Customer> findFullCustomerSecondPart(@Param("customerId")long id);
//
//    default Optional<Customer> findFullCustomer(long id){
//        Optional<Customer> customer = findFullCustomerFirstPart(id);
//
//    }
}
