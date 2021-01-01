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
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler{


	    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	    
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
	        System.out.println(request.getRequestURI());
	        if(targetUrl != null) {
	        	
	        	redirectStrategy.sendRedirect(request, response, targetUrl);
	        }
	        else {
	        	redirectStrategy.sendRedirect(request, response, request.getRequestURI());
	        }
	    }

	    
	    protected String determineTargetUrl(final Authentication authentication) {

	        Map<String, String> roleTargetUrlMap = new HashMap<>();
	        
	        //TODO Redirect back to origin page for customer
	        roleTargetUrlMap.put("CUSTOMER", null);
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
	    
	    protected void clearAuthenticationAttributes(HttpServletRequest request) {
	        HttpSession session = request.getSession(false);
	        if (session == null) {
	            return;
	        }
	        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	    }
	    
}
