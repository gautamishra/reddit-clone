package com.learning.redit.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.redit.exception.RedditException;
import com.learning.redit.model.RefreshToken;
import com.learning.redit.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
		
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());
		return refreshTokenRepository.save(refreshToken);
		}
	
	
	public void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token)
		.orElseThrow(() -> new RedditException("Invalid refresh Token"));
		}
	
	@Transactional
	public void deleteRefreshToken(String token) {
	refreshTokenRepository.deleteByToken(token);
	}
}
