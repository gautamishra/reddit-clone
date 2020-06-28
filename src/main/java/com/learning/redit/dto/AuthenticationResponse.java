package com.learning.redit.dto;

public class AuthenticationResponse {
	  private String authenticationToken;
	  private String username;
	 
  public AuthenticationResponse(String authToken , String username){
	  this.authenticationToken = authToken;
	  this.username = username;
  }
	  
	public String getAuthenticationToken() {
		return authenticationToken;
	}
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	  
	  
}
