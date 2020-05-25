package com.learning.redit.service;

import static com.learning.redit.util.Constants.ACTIVATION_EMAIL;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learning.redit.dto.RegisterRequest;
import com.learning.redit.exception.RedditException;
import com.learning.redit.modal.NotificationEmail;
import com.learning.redit.modal.User;
import com.learning.redit.modal.VerificationToken;
import com.learning.redit.repository.UserRepository;
import com.learning.redit.repository.VerificationtokenRepository;

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
}
