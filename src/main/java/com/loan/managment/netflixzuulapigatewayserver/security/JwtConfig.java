package com.loan.managment.netflixzuulapigatewayserver.security;

import org.springframework.beans.factory.annotation.Value;

public class JwtConfig {

	public void setUri(String uri) {
		Uri = uri;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return "JwtConfig [Uri=" + Uri + ", header=" + header + ", prefix=" + prefix + ", expiration=" + expiration
				+ ", secret=" + secret + "]";
	}
 
	
	@Value("${security.jwt.uri:/loan-management/login}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;
    
    // In case you want to use plain getters instead of lombok.
	public String getUri() {
		return Uri;
	}

	public String getHeader() {
		return header;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getExpiration() {
		return expiration;
	}

	public String getSecret() {
		return secret;
	}
    
}
