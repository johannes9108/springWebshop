package springWebshop.application.model.domain;

import lombok.*;
import springWebshop.application.model.domain.segmentation.ProductCategory;
import springWebshop.application.model.domain.segmentation.ProductSubCategory;
import springWebshop.application.model.domain.segmentation.ProductType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private double basePrice;
	private double vatPercentage;
	private String name;
	private String description;
	private String fullImageUrl;
	private String thumbnailUrl;
	private boolean published;

	@ManyToOne
	private ProductType productType;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return id == product.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public boolean updateProductCat(ProductCategory newCat) {
		
	return true;
	}
	public boolean updateProductSub(ProductSubCategory newSub) {
		
		return true;
	}
	public boolean updateProductType(ProductType newType) {
		productType.getProductSubCategory().addProductType(newType);
		productType.getProductSubCategory().removeProductType(productType);
		productType = newType;
		return true;
	}
	
}
