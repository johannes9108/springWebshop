package springWebshop.application.integration.account;

import org.springframework.stereotype.Repository;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.CustomerAddress;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {
    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<Customer> findByIdFullFetch(Long id) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            Root<Customer> customer = criteriaQuery.from(Customer.class);
            Fetch<Customer, CustomerAddress> fetch = customer.fetch("addresses", JoinType.LEFT);
            
//            	System.out.println("No addresses associated for customer with ID: " + id);
//                customer = criteriaQuery.from(Customer.class);
//                throw new RuntimeException("No addresses associated");
                
          

            criteriaQuery.select(customer)
            .where(criteriaBuilder.equal(customer.get("id"),id))
            .distinct(true);
            TypedQuery<Customer> typedQuery = em.createQuery(criteriaQuery);
            Customer c = typedQuery.getSingleResult();
            System.out.println("SUCCESSFUL FETCH CUSTOMER:" + c);
            return Optional.of(c);
        } catch (Exception e) {
        	System.out.println(e);
            return Optional.empty();
        }
    }
}
