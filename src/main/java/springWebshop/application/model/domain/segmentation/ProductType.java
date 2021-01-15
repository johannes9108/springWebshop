package springWebshop.application.model.domain.segmentation;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
/**
 * Product type is most specific (lowest) level of product segmentation
 * which is a child entity of Product Subcategory.
 */
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    
    private long categoryId;
    private long subCategoryId;
    
    private String name;

    @ManyToOne
    private ProductSubCategory productSubCategory;

    public ProductType(String name, ProductSubCategory productSubCategory) {
    	this.subCategoryId = productSubCategory.getId();
    	this.categoryId = productSubCategory.getProductCategory().getId();
        this.name = name;
        this.productSubCategory = productSubCategory;
    }

	@Override
	public String toString() {
		return "Type:"+name+"|"+productSubCategory.toString();
	}
    
//	public String getFullyQualifiedName() {
//		return productSubCategory.getFullyQualifiedName() + "/" +  name;
//	}

	@Override
	public boolean equals(Object obj) {
		ProductType other = (ProductType) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
    

}
