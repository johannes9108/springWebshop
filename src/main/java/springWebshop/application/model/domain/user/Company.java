package springWebshop.application.model.domain.user;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ConstraintMode;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Companies")
public class Company{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String VAT;
	private String phoneNumber;
	private String mobileNumber;
	
//	@ElementCollection(fetch = FetchType.EAGER)
//	private List<Address> addresses = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
	@JoinColumn(name = "companyId")
	private List<Customer> customers = new ArrayList<>();

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", VAT=" + VAT + ", phoneNumber=" + phoneNumber
				+ ", mobileNumber=" + mobileNumber +"]";
	}
	
	
	
	
	
	
}
