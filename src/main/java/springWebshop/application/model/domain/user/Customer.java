package springWebshop.application.model.domain.user;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import springWebshop.application.model.domain.Order;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "customers")
public class Customer extends Account implements Serializable {
	
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)//, mappedBy = "customer")
	private List<CustomerAddress> addresses;

//	@OneToMany (mappedBy = "customer")
//	private List<Order> orders;

//	@Override
//	public String toString() {
//		return "Customer [addresses="  + " toString()=" + super.toString() + "]";
//	}
	
	public Customer() {
		addresses = new ArrayList<>();
	}
	
	
	

	public void addAddress(CustomerAddress address){
		if (this.addresses.isEmpty()) address.setAsDefault();
		this.addresses.add(address);
		address.setCustomer(this);
	}




	public Customer(String firstName, String lastName, String password, String email, String phoneNumber,
			String mobileNumber, List<CustomerAddress> addresses) {
		super(firstName, lastName, password, email, phoneNumber, mobileNumber);
		this.addresses = addresses;
	}
}
