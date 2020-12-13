package springWebshop.application.integration;

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

import springWebshop.application.model.domain.Product;
import springWebshop.application.service.product.ProductSearchConfig;
public class ProductRepositoryCustomImpl extends AbstractCustomRepository<Product> implements ProductRepositoryCustom {


	@Override
	public Page<Product> getProducts(ProductSearchConfig config, int page, int size) {
		
		//TODO Not working properly, returning some correct data but missing some when filtering
		// by subcategory and below
		List<Predicate> predicates = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
			Root<Product> product = criteriaQuery.from(Product.class);
			System.out.println("Config:"+config);
			if (config.getProductTypeId() > 0) {
//				ArrayList<Long> longArray;
//				criteriaBuilder.isMember(product.get("productType").get("id"), product)
				predicates.add(criteriaBuilder.equal(
						product.get("productType").get("id"),
						config.getProductTypeId()));
				System.out.println("Filter by Type");
			}
			else if (config.getProductSubCategoryId() > 0) {
				predicates.add(criteriaBuilder.equal(
						product.get("productType").get("productSubCategory").get("id"),
						config.getProductSubCategoryId()));
				
				System.out.println("Filter by SubCategory");
			}
			else if (config.getProductCategoryId() > 0) {
				predicates.add(criteriaBuilder.equal(
						product.get("productType").get("productSubCategory").get("productCategory").get("id"),
						config.getProductCategoryId()));
				System.out.println("Filter by Category");
				
			}
			if(config.getSearchString()!=null && config.getSearchString().length()>0) {
				predicates.add(criteriaBuilder.like(product.get("name"), "%" + config.getSearchString() + "%"));
				
			}
			criteriaQuery.select(product).distinct(true).where(predicates.toArray(new Predicate[0]));
			TypedQuery<Product> typedQuery = em.createQuery(criteriaQuery);
			Page<Product> tmp = getPaginatedResult(page, size, predicates, typedQuery, Product.class);
			System.out.println("ReturningPage:");
			tmp.getContent().forEach(System.out::println);
			
			return tmp;
		} catch (NoResultException e) {
			System.out.println("Exception:" + e);
			return null;
		}

	}

//	private Page<Product> getPaginatedResults(int page, int size, List<Predicate> predicates,
//			 TypedQuery<Product> typedQuery) {
//		System.out.println("Local Results");
//		int firstResult = page * size;
//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
//		countQuery.select(criteriaBuilder.count(countQuery.from(Product.class)))
//				.where(predicates.toArray(new Predicate[0]));
//		
//		Long totalItems = em.createQuery(countQuery).getSingleResult();
//
//		typedQuery.setFirstResult(firstResult);
//		typedQuery.setMaxResults(size);
//
//		List<Product> productList = typedQuery.getResultList();
//		
//		Page<Product> newPage = new PageImpl<>(productList, PageRequest.of(page, size), totalItems);
//		System.out.println(newPage);
//
//		return newPage;
//	}
	

	

//	@Override
//    public Optional<List<Product>> findProductsByCategoryId(long categoryId) {
//        
//        
//         try {
//                CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//                CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
//                Root<Product> product = criteriaQuery.from(Product.class);
////                product.fetch("productType", JoinType.INNER);
//                Join<Product, ProductCategory> categories = product.join("productType").join("productSubCategory").join("productCategory");
//                
//                criteriaQuery.select(product)
//                        .where(criteriaBuilder.equal(categories.get("id"), categoryId))
//                        .distinct(true);
//                TypedQuery<Product> typedQuery = em.createQuery(criteriaQuery);
//                
//                
//                return Optional.of(typedQuery.getResultList());
//            } catch (NoResultException e) {
//                return Optional.empty();
//            }
//    }

// 
//
//    @Override
//    public Optional<List<Product>> findProductsBySubCategoryId(long subCategoryId) {
//         try {
//                CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//                CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
//                Root<Product> product = criteriaQuery.from(Product.class);
////                product.fetch("productType", JoinType.INNER);
//                Join<Product, ProductSubCategory> subCategories = product.join("productType").join("productSubCategory");
//                
//                criteriaQuery.select(product)
//                        .where(criteriaBuilder.equal(subCategories.get("id"), subCategoryId))
//                        .distinct(true);
//                TypedQuery<Product> typedQuery = em.createQuery(criteriaQuery);
//                
//                
//                return Optional.of(typedQuery.getResultList());
//            } catch (NoResultException e) {
//                return Optional.empty();
//            }
//    }
////
////
//    @Override
//    public Optional<List<Product>> findProductsByTypeId(long typeId) {
//         try {
//                CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//                CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
//                Root<Product> product = criteriaQuery.from(Product.class);
////                product.fetch("productType", JoinType.INNER);
//                Join<Product, ProductType> types = product.join("productType");
//                
//                criteriaQuery.select(product)
//                        .where(criteriaBuilder.equal(types.get("id"), typeId))
//                        .distinct(true);
//                TypedQuery<Product> typedQuery = em.createQuery(criteriaQuery);
//                
//                
//                return Optional.of(typedQuery.getResultList());
//            } catch (NoResultException e) {
//                return Optional.empty();
//            }
//    }

}
