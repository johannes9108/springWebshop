package springWebshop.application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SegmentDTO {
	
	public SegmentDTO(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	private long id;
	private String name;
	@Override
	public String toString() {
		return "SegmentDTO [id=" + id + ", name=" + name + "]";
	}

	
}
