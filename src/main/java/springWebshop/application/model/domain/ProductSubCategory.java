package springWebshop.application.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GeneratorType;

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
    long id;

    @NotBlank
    String name;

    @ManyToOne
    @NotNull
    private ProductCategory productCategory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE,
    mappedBy = "productSubCategory")
    private Set<ProductType> productTypes;


    public ProductSubCategory(String name, ProductCategory productCategory) {
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
    public String toString() {
        return "ProductSubCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
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

	public @NotBlank String getFullyQualifiedName() {
		return productCategory.getFullyQualifiedName() + "/" + name ;
	}
}
