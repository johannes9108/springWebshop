package springWebshop.application.integration.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;

import springWebshop.application.integration.AbstractCustomRepository;
import springWebshop.application.model.domain.Product;
import springWebshop.application.service.product.ProductSearchConfig;
public class ProductRepositoryCustomImpl extends AbstractCustomRepository<Product> implements ProductRepositoryCustom {


	@Override
	public Page<Product> getProducts(ProductSearchConfig config, int page, int size) {
		
		List<Predicate> predicates = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
			Root<Product> product = criteriaQuery.from(Product.class);
			System.out.println("Config:"+config);
			if (config.getProductTypeId() > 0) {
				predicates.add(criteriaBuilder.equal(
						product.get("productType").get("id"),
						config.getProductTypeId()));
			}
			else if (config.getProductSubCategoryId() > 0) {
				predicates.add(criteriaBuilder.equal(
						product.get("productType").get("subCategoryId"),
						config.getProductSubCategoryId()));
				
			}
			else if (config.getProductCategoryId() > 0) {
				predicates.add(criteriaBuilder.equal(
						product.get("productType").get("categoryId"),
						config.getProductCategoryId()));
			}
			if(config.getSearchString()!=null && config.getSearchString().length()>0) {
				predicates.add(criteriaBuilder.like(product.get("name"), "%" + config.getSearchString() + "%"));
			}
			if(config.isPublished()) {
				predicates.add(criteriaBuilder.equal(product.get("published"),true));
			}
			criteriaQuery.select(product).distinct(true).where(predicates.toArray(new Predicate[0]));
			TypedQuery<Product> typedQuery = em.createQuery(criteriaQuery);
			return getPaginatedResult(page, size, predicates, typedQuery,Product.class);
		} catch (NoResultException e) {
			System.out.println("Exception:" + e);
			return null;
		}

	}

}
