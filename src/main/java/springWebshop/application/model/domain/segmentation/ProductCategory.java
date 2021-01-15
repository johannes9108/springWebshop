package springWebshop.application.model.domain.segmentation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE,
    mappedBy = "productCategory")
    private Set<ProductSubCategory> subCategories;

    public ProductCategory() {
        this.subCategories = new HashSet<>();
    }

    public ProductCategory(String name) {
        this.subCategories = new HashSet<>();
        this.name = name;
    }

    public void addSubcategory(ProductSubCategory productSubCategory) {
        subCategories.add(productSubCategory);
    }

    public void removeSubcategory(ProductSubCategory productSubCategory) {
        subCategories.remove(productSubCategory);
    }

    @Override
    public boolean equals(Object o) {
        ProductCategory that = (ProductCategory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category:"+name;
        
    }
//	public String getFullyQualifiedName() {
//		return name;
//	}
}
