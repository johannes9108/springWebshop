package springWebshop.application.service.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import springWebshop.application.integration.account.AccountRepository;
import springWebshop.application.integration.account.AdminRepository;
import springWebshop.application.integration.account.CustomerRepository;
import springWebshop.application.integration.account.RoleRepository;
import springWebshop.application.model.domain.user.Account;
import springWebshop.application.model.domain.user.Admin;
import springWebshop.application.model.domain.user.Customer;
import springWebshop.application.model.domain.user.ERole;
import springWebshop.application.model.domain.user.Role;
import springWebshop.application.service.ServiceErrorMessages;
import springWebshop.application.service.ServiceResponse;


@Service
public class AccountService {

    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;
    private AdminRepository adminRepository;

    public AccountService(CustomerRepository customerRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.adminRepository = adminRepository;
    }

    public ServiceResponse<Customer> createCustomer(Customer customer) {
        ServiceResponse<Customer> response = new ServiceResponse<>();
        List<String> errors = new ArrayList<>();

        if (!accountExist(customer, errors) && hasValidBasicData(customer, errors))
            try {
                customer.setPassword(passwordEncoder.encode(customer.getPassword()));
                Role customerRole = roleRepository.findByName(ERole.CUSTOMER).get();
                Set<Role> roleList = new HashSet<>(Arrays.asList(customerRole));
                customer.setRoles(roleList);
                response.addResponseObject(customerRepository.save(customer));
            } catch (Exception e) {
                response.addErrorMessage(ServiceErrorMessages.CUSTOMER.couldNotCreate());
            }
        response.setErrorMessages(errors);
        return response;
    }

    public ServiceResponse<Customer> updateCustomer(Customer updatedCustomer){

    	 ServiceResponse<Customer> response = new ServiceResponse<>();
         List<String> errors = new ArrayList<>();
             try {
            	 Optional<Customer> persistedCustomer = customerRepository.findById(updatedCustomer.getId());
            	 if(persistedCustomer.isPresent()) {
            		 Customer c = updatedCustomer;
            		 response.addResponseObject(customerRepository.save(c));
            	 }
            	 else {
            		 response.addErrorMessage(ServiceErrorMessages.CUSTOMER.couldNotUpdate(updatedCustomer.getId()));
            	 }
             } catch (Exception e) {
                 response.addErrorMessage(ServiceErrorMessages.CUSTOMER.couldNotUpdate(updatedCustomer.getId()));
             }
         response.setErrorMessages(errors);
         return response;
    }
    
    
    public ServiceResponse<Admin> createAdmin(Admin admin) {
        ServiceResponse<Admin> response = new ServiceResponse<>();
        List<String> errors = new ArrayList<>();

        if (!accountExist(admin, errors) && hasValidBasicData(admin, errors))
            try {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                Role adminRole = roleRepository.findByName(ERole.ADMIN).get();
                Set<Role> roleList = new HashSet<>(Arrays.asList(adminRole));
                admin.setRoles(roleList);
                response.addResponseObject(adminRepository.save(admin));
            } catch (Exception e) {
                response.addErrorMessage(ServiceErrorMessages.ADMIN.couldNotCreate());
            }
        response.setErrorMessages(errors);
        return response;
    }

    boolean hasValidBasicData(Account account, List<String> errors) {
        if (account.getEmail() != null && account.getPassword().length()>5) {
            return true;
        } else {
            errors.add("Provide a valid email and password with minimum 6 characters");
            return false;
        }
    }

    public ServiceResponse<Customer> getCustomerById(long id) {
        ServiceResponse<Customer> response = new ServiceResponse<>();
        try {
            Optional<Customer> customer = customerRepository.findById(id);
            if (!customer.isPresent()) {
                response.addErrorMessage(ServiceErrorMessages.CUSTOMER.couldNotFind(id));
            } else {
                response.addResponseObject(customer.get());
            }
        } catch (Exception e) {
            response.addErrorMessage(ServiceErrorMessages.CUSTOMER.couldNotFind(id));
        }
        return response;
    }

    public ServiceResponse<Admin> getAdminById(long id) {
        ServiceResponse<Admin> response = new ServiceResponse<>();
        try {
            Optional<Admin> admin = adminRepository.findById(id);
            if (!admin.isPresent()) {
                response.addErrorMessage(ServiceErrorMessages.ADMIN.couldNotFind(id));
            } else {
                response.addResponseObject(admin.get());
            }
        } catch (Exception e) {
            response.addErrorMessage(ServiceErrorMessages.ADMIN.couldNotFind(id));
        }
        return response;
    }

    private boolean accountExist(Account account, List<String> errors) {
        if (accountRepository.existsAccountByEmail(account.getEmail())) {
            errors.add("Account with that email " + account.getEmail() + " already exist");
            return true;
        } else {
            return false;
        }
    }
    
    
}
