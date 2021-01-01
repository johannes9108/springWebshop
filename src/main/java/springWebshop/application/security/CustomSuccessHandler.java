package springWebshop.application.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	 private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	 public CustomSuccessHandler() {
		 super();
		 setUseReferer(true);
	 }
	 
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
      HttpServletResponse response, Authentication authentication)
      throws IOException {
		
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
    
    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response, 
            Authentication authentication
    ) throws IOException {
    	System.out.println(authentication.getPrincipal());
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            System.out.println(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }
       
        	HttpSession session = request.getSession();
        	// Before SS 3.0
//        	SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST_KEY");
        	SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        	if(savedRequest!=null) {
        		redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        	}else {
        		redirectStrategy.sendRedirect(request, response, targetUrl);
        		
        	}
        
    }

    
    protected String determineTargetUrl(final Authentication authentication) {

        Map<String, String> roleTargetUrlMap = new HashMap<>();
        
        //TODO Redirect back to origin page for customer
        roleTargetUrlMap.put("CUSTOMER", "/webshop/profile");
        roleTargetUrlMap.put("ADMIN", "/webshop/admin/products");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if(roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }
	
	
	
	

}
