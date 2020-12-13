package springWebshop.application.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springWebshop.application.integration.account.AccountRepository;
import springWebshop.application.model.domain.user.Account;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final
    AccountRepository userRepository;

    public UserDetailsServiceImpl(AccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Hittade ingen användare med det användarnamnet."));

        return UserDetailsImpl.build(user);
    }
}