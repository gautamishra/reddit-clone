package com.learning.redit.service;


import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.redit.modal.User;
import com.learning.redit.repository.UserRepository;
import static java.util.Collections.singletonList;

@Service
public class UserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> userOptionl = userRepository.findByUsername(username);
		
		User user = userOptionl.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username : " + username));
		 return new org.springframework.security
	                .core.userdetails.User(user.getUsername(), user.getPassword(),
	                user.isEnabled(), true, true,
	                true, getAuthorities("USER"));
	    }

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
	    return singletonList(new SimpleGrantedAuthority(role));
	}

}
