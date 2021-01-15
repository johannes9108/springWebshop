package springWebshop.application.integration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.xml.xpath.XPathFactory;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Activator;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.ClassUtils;

import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.order.Order;

import static javax.xml.xpath.XPathFactory.newInstance;

@Repository
//@NoArgsConstructor
public abstract class AbstractCustomRepository<T> {
	
	@PersistenceContext
	protected
	EntityManager em;

	protected Page<T> getPaginatedResult(int page, int size, List<Predicate> predicates,
			 TypedQuery<T> typedQuery, Class rootClass) {

		int firstResult = page * size;
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(rootClass)))
				.where(predicates.toArray(new Predicate[0]));
		
		Long totalItems = em.createQuery(countQuery).getSingleResult();

		typedQuery.setFirstResult(firstResult);
		typedQuery.setMaxResults(size);

		List<T> productList = typedQuery.getResultList();
		
		Page<T> newPage = new PageImpl<>(productList, PageRequest.of(page, size), totalItems);
		System.out.println(newPage);

		return newPage;
	}
	

}
