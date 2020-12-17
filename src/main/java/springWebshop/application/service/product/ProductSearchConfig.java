package springWebshop.application.service.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchConfig {
    long productCategoryId;
    long productSubCategoryId;
    long productTypeId;
    String searchString;
    
    public ProductSearchConfig() {
    	searchString = "";
    }

	@Override
	public String toString() {
		return "ProductSearchConfig [productCategoryId=" + productCategoryId + ", productSubCategoryId="
				+ productSubCategoryId + ", productTypeId=" + productTypeId + ", searchString=" + searchString + "]";
	}
}
