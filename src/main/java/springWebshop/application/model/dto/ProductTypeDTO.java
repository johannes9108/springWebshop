package springWebshop.application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTypeDTO {
	private long id;
	private String name;
	public ProductTypeDTO(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return "ProductTypeDTO [id=" + id + ", name=" + name + "]";
	}

}
