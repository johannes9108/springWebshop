package springWebshop.application.integration;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;
import springWebshop.application.model.dto.SegmentDTO;

@Repository
public class SegmentationDTORepositoryImpl implements SegmentationDTORepository{
	
	@PersistenceContext
	EntityManager em;
	@Override
	public List<SegmentDTO> getAllCategoryDTO() {
		
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<SegmentDTO> query = builder.createQuery(SegmentDTO.class);
			Root<ProductCategory> category = query.from(ProductCategory.class);
			query.multiselect(category.get("id"),category.get("name"));
			TypedQuery<SegmentDTO> typedQuery = em.createQuery(query);
			typedQuery.setFirstResult(1);
			return typedQuery.getResultList();
		}
		catch(NoResultException e) {
			System.out.println(e);
			return new ArrayList<>();
		}
	
	
	}

	@Override
	public List<SegmentDTO> getAllSubCategoryDTO(long categoryId) {

		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<SegmentDTO> query = builder.createQuery(SegmentDTO.class);
			Root<ProductSubCategory> subCategory = query.from(ProductSubCategory.class);
			Join<ProductSubCategory, ProductCategory> category = subCategory.join("productCategory",JoinType.INNER);
			query.where(builder.equal(category.get("id"),categoryId))
			.multiselect(subCategory.get("id"),subCategory.get("name"));
			TypedQuery<SegmentDTO> typedQuery = em.createQuery(query);
			return typedQuery.getResultList();
					
		}
		catch(NoResultException e) {
			System.out.println(e);
			return new ArrayList<>();
		}
	}
	@Override
	public List<SegmentDTO> getAllTypeDTO(long subCategoryId) {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<SegmentDTO> query = builder.createQuery(SegmentDTO.class);
			Root<ProductType> type = query.from(ProductType.class);
			Join<ProductType, ProductSubCategory> subCategory = type.join("productSubCategory",JoinType.INNER);
			query = query.where(builder.equal(subCategory.get("id"),subCategoryId));
			Join<ProductSubCategory,ProductCategory>category = subCategory.join("productCategory",JoinType.INNER);
			
			query
			.multiselect(type.get("id"),type.get("name"));
			TypedQuery<SegmentDTO> typedQuery = em.createQuery(query);
			return typedQuery.getResultList();
					
					
		}
		catch(NoResultException e) {
			System.out.println(e);
			return new ArrayList<>();
		}
	}

	public List<SegmentDTO> getAllSubCategoryDTO() {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<SegmentDTO> query = builder.createQuery(SegmentDTO.class);
			Root<ProductSubCategory> category = query.from(ProductSubCategory.class);
			query.multiselect(category.get("id"),category.get("name"));
			TypedQuery<SegmentDTO> typedQuery = em.createQuery(query);
			typedQuery.setFirstResult(1);
			return typedQuery.getResultList();
		}
		catch(NoResultException e) {
			System.out.println(e);
			return new ArrayList<>();
		}
	}

	public List<SegmentDTO> getAllTypeDTO() {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<SegmentDTO> query = builder.createQuery(SegmentDTO.class);
			Root<ProductType> category = query.from(ProductType.class);
			query.multiselect(category.get("id"),category.get("name"));
			TypedQuery<SegmentDTO> typedQuery = em.createQuery(query);
			typedQuery.setFirstResult(1);
			return typedQuery.getResultList();
		}
		catch(NoResultException e) {
			System.out.println(e);
			return new ArrayList<>();
		}
	}

}
