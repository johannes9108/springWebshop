package springWebshop.application.model.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.model.domain.segmentation.ProductCategory;

@Getter
@Setter
public class SegmentationModelObject {
	
	


	@Override
	public String toString() {
		return "CategoryModelObject [selectedCat=" + selectedCat + ", selectedSub=" + selectedSub + ", selectedType="
				+ selectedType + ", categories=" + categories + ", subCategories=" + subCategories + ", types=" + types
				+ "]";
	}

	private long selectedCat;
	private long selectedSub;
	private long selectedType;
	
	private List<SegmentDTO> categories;
	private List<SegmentDTO> subCategories;
	private List<SegmentDTO> types;
	
	public SegmentationModelObject() {
		categories = new  ArrayList<>();
		subCategories = new  ArrayList<>();
		types = new  ArrayList<>();
		
	}


}
