package springWebshop.application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import springWebshop.application.model.domain.user.ERole;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/",
                        "/h2-console/**",
                        "/css/**",
                        "/resources/**",
                        "/webshop/api/**",
                        "/webshop/products/**",
                        "/webshop/login",
                        "/webshop/register",
                        "/webshop/shoppingcart/**").permitAll()
                .antMatchers("/webshop/admin/**").hasAuthority(ERole.ADMIN.name())
                .antMatchers("/webshop/checkout/**").hasAuthority(ERole.CUSTOMER.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/webshop/login")
                	.loginProcessingUrl("/webshop/processLogin")
//                	.defaultSuccessUrl("/webshop/products", true)
                	.successHandler(successHandler())
                	.failureUrl("/webshop/login?error=true")
                	.permitAll()
                	.and()
                .logout()
                	.logoutUrl("/webshop/logout")
                	.logoutSuccessUrl("/webshop/login")
                .deleteCookies("JSESSIONID")
                .permitAll();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Bean
    public AuthenticationSuccessHandler successHandler() {
    	return new SimpleUrlAuthenticationSuccessHandler();
    }

}