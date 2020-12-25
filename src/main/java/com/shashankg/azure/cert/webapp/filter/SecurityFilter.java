package com.shashankg.azure.cert.webapp.filter;

import com.shashankg.azure.cert.webapp.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(22)
public class SecurityFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Initializing SecurityFilter.");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getCredentials() instanceof Jwt) {
			Jwt jwt = (Jwt) authentication.getCredentials();
			User user = new User(jwt.getClaim("name"), jwt.getClaim("preferred_username"));
			servletRequest.setAttribute("user", user);
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
