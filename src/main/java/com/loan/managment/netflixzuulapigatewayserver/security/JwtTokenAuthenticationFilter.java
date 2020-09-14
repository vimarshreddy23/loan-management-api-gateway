package com.loan.managment.netflixzuulapigatewayserver.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtTokenAuthenticationFilter extends  OncePerRequestFilter {
    
	Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);
	private final JwtConfig jwtConfig;
	
	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		// 1. get the authentication header. Tokens are supposed to be passed in the authentication header
		
		String header = request.getHeader(jwtConfig.getHeader());
		logger.debug("Header::::: "+header);
		// 2. validate the header and check the prefix
		if(header == null || !header.startsWith(jwtConfig.getPrefix())) {
			chain.doFilter(request, response);  		// If not valid, go to the next filter.
			return;
		}
		logger.debug("here $$$$$$$$$$$$$$$$$$$$");
		// If there is no token provided and hence the user won't be authenticated. 
		// It's Ok. Maybe the user accessing a public path or asking for a token.
		
		// All secured paths that needs a token are already defined and secured in config class.
		// And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.
		
		// 3. Get the token
		String token = header.replace(jwtConfig.getPrefix(), "");
		logger.debug("token $$$$$$$$$$$$$$$$$$$$     - "+token);
		
		try {	// exceptions might be thrown in creating the claims if for example the token is expired
			
			// 4. Validate the token
			Claims claims = Jwts.parser()
					.setSigningKey(jwtConfig.getSecret())
					.parseClaimsJws(token)
					.getBody();
			
			String username = claims.getSubject();
			logger.debug("username $$$$$$$$$$$$$$$$$$$$     - "+username);
			if(username != null) {
				
				String[] authList = {"ROLE_ADMIN", "ROLE_USER"};
				  List<String> authorities = Arrays.asList(authList);
				 
				
				// 5. Create auth object
				// UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
				// It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
				 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							username,
							null
								  , authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.
								  toList())
								 );
				 
				 // 6. Authenticate the user
				 // Now, user is authenticated
					logger.debug("auth $$$$$$$$$$$$$$$$$$$$     - "+auth);
				 SecurityContextHolder.getContext().setAuthentication(auth);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// In case of failure. Make sure it's clear; so guarantee user won't be authenticated
			SecurityContextHolder.clearContext();
		}
		logger.debug("About to call filter");
		// go to the next filter in the filter chain
		chain.doFilter(request, response);
	}

}