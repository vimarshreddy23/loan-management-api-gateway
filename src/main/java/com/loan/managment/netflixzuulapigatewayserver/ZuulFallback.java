package com.loan.managment.netflixzuulapigatewayserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ZuulFallback implements FallbackProvider {
	 
	    private static final String DEFAULT_MESSAGE = "Sorry, requested service is not available, please try later";
	 
	    @Override
	    public String getRoute() {
	        return "*";
	    }
	
	    
	    @Override
	    public ClientHttpResponse fallbackResponse(String route, Throwable throwable) {
	        return new ClientHttpResponse() {
	            @Override
	            public HttpStatus getStatusCode() throws IOException {
	                return HttpStatus.OK;
	            }

	            @Override
	            public int getRawStatusCode() throws IOException {
	                return 200;
	            }

	            @Override
	            public String getStatusText() throws IOException {
	                return "OK";
	            }

	            @Override
	            public void close() {

	            }

	            @Override
	            public InputStream getBody() throws IOException {
	                return new ByteArrayInputStream(DEFAULT_MESSAGE.getBytes());
	            }

	            @Override
	            public HttpHeaders getHeaders() {
	                HttpHeaders headers = new HttpHeaders();
	                headers.setContentType(MediaType.APPLICATION_JSON);
	                return headers;
	            }
	        };
	    }
	 
	}


