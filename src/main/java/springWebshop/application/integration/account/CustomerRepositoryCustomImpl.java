package springWebshop.application.integration.account;

import org.springframework.stereotype.Repository;
import springWebshop.application.model.domain.user.Customer;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
            customer.fetch("addresses", JoinType.INNER);
            criteriaQuery.select(customer)
                    .where(criteriaBuilder.equal(customer.get("id"),id))
                    .distinct(true);
            TypedQuery<Customer> typedQuery = em.createQuery(criteriaQuery);
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
