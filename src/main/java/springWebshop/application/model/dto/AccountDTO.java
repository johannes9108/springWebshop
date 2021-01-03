package springWebshop.application.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import springWebshop.application.service.user.PasswordMatches;
import springWebshop.application.service.user.ValidEmail;
import springWebshop.application.service.user.ValidPassword;
@Getter
@Setter
@PasswordMatches
public class AccountDTO {
	
	@NotNull
	@NotEmpty
	private String firstName;
	
	@NotBlank(message = "Please provide last name.")
	private String lastName;

	@NotBlank
	@Size(min = 6, message = "Password must be minimum 6 characters.")
	@ValidPassword
	private String password;
	
	@ValidPassword
	private String repeatPassword;

	@NotBlank(message = "Email can't be empty.")
	@ValidEmail
	private String email;
	
	private String username;
	private String phoneNumber;
	private String mobileNumber;

}
