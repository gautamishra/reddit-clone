package com.learning.redit.service;

import static com.learning.redit.util.Constants.ACTIVATION_EMAIL;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.redit.dto.AuthenticationResponse;
import com.learning.redit.dto.LoginRequest;
import com.learning.redit.dto.RefreshTokenRequest;
import com.learning.redit.dto.RegisterRequest;
import com.learning.redit.exception.RedditException;
import com.learning.redit.modal.NotificationEmail;
import com.learning.redit.modal.User;
import com.learning.redit.modal.VerificationToken;
import com.learning.redit.repository.UserRepository;
import com.learning.redit.repository.VerificationtokenRepository;
import com.learning.redit.security.JwtProvider;

@Service
public class AuthServcie {
	
	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
    private  PasswordEncoder passwordEncoder;
	
	@Autowired
	private VerificationtokenRepository verificationRepository;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private MailContentBuilder mailContentBuilder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
      
        String token = generateVerification(user);
        
        String message = mailContentBuilder.build("Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
        		+ ACTIVATION_EMAIL + "/" + token);
        NotificationEmail notificationEmail = new NotificationEmail("Please Activate your account", user.getEmail(), message);
        mailService.sendMail(notificationEmail);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    private String  generateVerification(User user) {
    	String token = UUID.randomUUID().toString();
    	VerificationToken verToken = new VerificationToken();
    	verToken.setToken(token);
    	verToken.setUser(user);
    	verificationRepository.save(verToken);
    	return token;
    }
    
    
    public AuthenticationResponse login(LoginRequest loginRequest) {
    	Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
    	loginRequest.getPassword()));
    	SecurityContextHolder.getContext().setAuthentication(authenticate);
    	String authenticationToken = jwtProvider.generateToken(authenticate);
    	return AuthenticationResponse.builder()
    			.authenticationToken(authenticationToken)
    			.refreshToken(refreshTokenService.generateRefreshToken().getToken())
    			.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
    			.username(loginRequest.getUsername())
    			.build();
    }
    
    public void verifyAccount(String token) {
    	System.out.println(token);
    	Optional<VerificationToken> verificationTokenOptional = verificationRepository.findByToken(token);
    	verificationTokenOptional.orElseThrow(() -> new RedditException("Invalid Token"));
    	fetchUserAndEnable(verificationTokenOptional.get());
    }
    
    
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RedditException("User Not Found with id - " + username));
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	
	@Transactional(readOnly = true)
	public User getCurrentUser() {
	org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
	getContext().getAuthentication().getPrincipal();
	return userRepository.findByUsername(principal.getUsername())
			.orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	}

	
	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
				
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.username(refreshTokenRequest.getUsername())
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.build();
	}
	
}
