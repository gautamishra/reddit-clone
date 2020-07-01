package com.learning.redit.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.learning.redit.exception.RedditException;
import com.learning.redit.service.AuthServcie;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Component
public class JwtProvider {
	
	@Value("${jwt.expiration.time}")
	private  long jwtExpirationInMillis;
	
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
		String jws = Jwts.builder()
				.setSubject(principal.getUsername())
				.signWith(getPrivateKey())
				.setExpiration(Date.from(Instant.now().plusMillis(getJwtExpirationInMillis())))
				.compact();
		return jws;
	}
	
	private PrivateKey getPrivateKey() {
		try {
		return (PrivateKey) keyStore.getKey("reditclone", "secret".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
		throw new RedditException("Exception occured while retrieving public key from keystore");
		}
	}
	
    public boolean validateToken(String jwt) {
    	 Jwts.parserBuilder().setSigningKey(getPublickey()).build().parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("reditclone").getPublicKey();
        } catch (KeyStoreException e) {
            throw new RedditException("Exception occured while retrieving public key from keystore");
        }
    }

    public String getUsernameFromJWT(String token) {
    	Claims claims = Jwts.parserBuilder()
    					.setSigningKey(getPublickey())
    					.build()
    					.parseClaimsJws(token)
    					.getBody();
        return claims.getSubject();
    }
    
    
	
    public Long getJwtExpirationInMillis() {
    	return jwtExpirationInMillis;
    	}

	public String generateTokenWithUserName(String username) {
		String jws = Jwts.builder()
				.setSubject(username)
				.signWith(getPrivateKey())
				.setExpiration(Date.from(Instant.now().plusMillis(getJwtExpirationInMillis())))
				.compact();
		return null;
	}
	
}
