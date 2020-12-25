package com.shashankg.azure.cert.webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.and()
				.authorizeRequests()
					.antMatchers( "/todo**", "/todo/**")
						.hasAuthority("SCOPE_user_access")
					.anyRequest()
						.authenticated()
			.and()
				.oauth2ResourceServer()
					.jwt();
	}
}