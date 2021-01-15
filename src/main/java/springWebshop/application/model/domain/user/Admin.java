package springWebshop.application.model.domain.user;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name ="Admins")
public class Admin extends Account {

	@Override
	public String toString() {
		return "Admin [toString()=" + super.toString() + "]";
	}
	
	
	
}
