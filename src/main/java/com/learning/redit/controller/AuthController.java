package com.learning.redit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redit.dto.AuthenticationResponse;
import com.learning.redit.dto.LoginRequest;
import com.learning.redit.dto.RegisterRequest;
import com.learning.redit.service.AuthServcie;

/**
 * Authentication process
 * @author gmishra
 *
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthServcie authService;
	
	@PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity("User Registration Success" , HttpStatus.OK);
    }
	
  @GetMapping("accountVerification/{token}")
  public ResponseEntity<String> verifyAccount(@PathVariable String token) {
  authService.verifyAccount(token);
  return new ResponseEntity<>("Account Activated Successully", HttpStatus.OK);
  }
	  
  @PostMapping("/login")
  public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
      return authService.login(loginRequest);
  }
	  
	
}
