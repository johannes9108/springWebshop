package springWebshop.application.model.viewModels;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SegmentationModelObject {
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

	@Override
	public String toString() {
		return "CategoryModelObject [selectedCat=" + selectedCat + ", selectedSub=" + selectedSub + ", selectedType="
				+ selectedType + ", categories=" + categories + ", subCategories=" + subCategories + ", types=" + types
				+ "]";
	}


}
