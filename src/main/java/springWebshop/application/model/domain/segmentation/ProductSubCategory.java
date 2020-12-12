package springWebshop.application.model.domain.segmentation;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GeneratorType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@Entity
@NoArgsConstructor
/**
 * Subcategory is the second highest level of product segmentation
 * which is a child entity of Product Category.
 */
public class ProductSubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    @NotNull
    private ProductCategory productCategory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE,
    mappedBy = "productSubCategory")
    private Set<ProductType> productTypes;


    public ProductSubCategory(String name, ProductCategory productCategory) {
    	this.productTypes = new HashSet<ProductType>();
        this.name = name;
        this.productCategory = productCategory;
    }

    public void addProductType(ProductType productType) {
        productTypes.add(productType);
    }

    public void removeProductType(ProductType productType) {
        productTypes.remove(productType);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSubCategory that = (ProductSubCategory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	public String getFullyQualifiedName() {
		return productCategory.getFullyQualifiedName() + "/" + name ;
	}

	@Override
	public String toString() {
		return "Subcategory:" + name + "|"+ productCategory.getName() + "]";
	}
}
