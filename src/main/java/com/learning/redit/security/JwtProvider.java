package com.learning.redit.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import io.jsonwebtoken.Jwts;
import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import com.learning.redit.exception.RedditException;


@Component
public class JwtProvider {
	
	private KeyStore keyStore;
	
	@PostConstruct
	public void init() {
	try {
	keyStore = KeyStore.getInstance("JKS");
	InputStream resourceAsStream = getClass().getResourceAsStream("/reditclone.jks");
	keyStore.load(resourceAsStream, "changeit".toCharArray());
	} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
	throw new RedditException("Exception occurred while loading keystore");
	}
}
	
	public String generateToken(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		System.out.println("Called here " + principal.getUsername());
		String jws = Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey()).compact();
		return jws;
	}
	
	private PrivateKey getPrivateKey() {
		try {
		return (PrivateKey) keyStore.getKey("reditclone", "secret".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
		throw new RedditException("Exception occured while retrieving public key from keystore");
		}
		}
}
