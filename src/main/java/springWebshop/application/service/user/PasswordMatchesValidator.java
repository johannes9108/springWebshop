package springWebshop.application.service.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import springWebshop.application.model.dto.AccountDTO;

public class PasswordMatchesValidator 
implements ConstraintValidator<PasswordMatches, Object> { 
  
  @Override
  public void initialize(PasswordMatches constraintAnnotation) {       
  }
  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context){   
      AccountDTO user = (AccountDTO) obj;
      return user.getPassword().equals(user.getRepeatPassword());    
  }     
}