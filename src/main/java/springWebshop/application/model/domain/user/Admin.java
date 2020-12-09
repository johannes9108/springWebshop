package springWebshop.application.model.domain.user;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Entity(name ="Admins")
public class Admin extends Account {

//	@ElementCollection
//	private List<ERole> roles;

	@Override
	public String toString() {
		return "Admin [toString()=" + super.toString() + "]";
	}
	
	
	
}
