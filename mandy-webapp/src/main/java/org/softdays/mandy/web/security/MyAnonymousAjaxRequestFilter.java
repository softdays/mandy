package org.softdays.mandy.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Prevents off-session Ajax request.
 * 
 * @author repatriarche
 *
 */
public class MyAnonymousAjaxRequestFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			chain.doFilter(request, response);
		} else {
			if (this.isAjax((HttpServletRequest)request)) {
				HttpServletResponse r = (HttpServletResponse) response;
				r.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

}
