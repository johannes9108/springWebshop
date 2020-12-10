package springWebshop.application.model.dto;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.model.domain.Product;
import springWebshop.application.model.domain.segmentation.ProductType;

import javax.persistence.ManyToOne;
import java.util.List;

@Getter @Setter
public class ProductFormModel {

	private Product domainProduct;

	private String newCategory;
	private String newType;
}
