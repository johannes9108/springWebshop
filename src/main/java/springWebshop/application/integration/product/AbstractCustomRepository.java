package springWebshop.application.integration.product;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Order;

@Repository
public abstract class AbstractCustomRepository<T> {
	
	@PersistenceContext
	protected
	EntityManager em;
//
//	final Class<T> typeParameterClass;
//
//	public AbstractCustomRepository(Class<T> typeParameterClass) {
//		this.typeParameterClass = typeParameterClass;
//	}
	
	protected Page<T> getPaginatedResult(int page, int size, List<Predicate> predicates,
			 TypedQuery<T> typedQuery) {
		int firstResult = page * size;
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(Order.class)))
				.where(predicates.toArray(new Predicate[0]));
		
		Long totalItems = em.createQuery(countQuery).getSingleResult();

		typedQuery.setFirstResult(firstResult);
		typedQuery.setMaxResults(size);

		List<T> productList = typedQuery.getResultList();
		
		Page<T> newPage = new PageImpl<>(productList, PageRequest.of(page, size), totalItems);
//		System.out.println(newPage);

		return newPage;
	}
	

}
