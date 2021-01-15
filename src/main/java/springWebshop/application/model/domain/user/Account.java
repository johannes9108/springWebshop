package springWebshop.application.model.domain.user;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class holding basic data for main user groups/entities; Admin and Customer.
 * Each entity is persisted in separate table per class.
 * Account is also the class parsed to UserDetails in Spring Security context.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Accounts")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotBlank(message = "Please provide first name.")
	private String firstName;
	@NotBlank(message = "Please provide first name.")
	private String lastName;

	@NotBlank()
	@Size(min = 6, message = "Password must be minimum 6 characters.")
	private String password;

	@NotBlank(message = "Email can't be empty.")
	@Email(message = "Please provide a valid email address.")
	private String email;
	private String username;
	private String phoneNumber;
	private String mobileNumber;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "account_roles",
			joinColumns = @JoinColumn(name = "account_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public void addRole(Role role){
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", password=" + password
				+ ", email=" + email + ", phoneNumber=" + phoneNumber + ", mobileNumber=" + mobileNumber + "]";
	}




	public Account(String firstName, String lastName, String password, String email, String phoneNumber,
			String mobileNumber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.mobileNumber = mobileNumber;
	}
	
	
}
