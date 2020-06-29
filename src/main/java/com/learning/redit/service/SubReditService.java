package com.learning.redit.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.redit.dto.SubReditDto;
import com.learning.redit.exception.SubReditNotFoundException;
import com.learning.redit.modal.Subreddit;
import com.learning.redit.repository.SubredditRepository;

@Service
public class SubReditService {
	
	@Autowired
	private SubredditRepository subReditRepository;
	
	@Autowired
	private AuthServcie authService;
	

    @Transactional(readOnly = true)
    public List<SubReditDto> getAll() {
        return subReditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
	

    @Transactional
    public SubReditDto save(SubReditDto subredditDto) {
        Subreddit subreddit = subReditRepository.save(mapToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }
	
    @Transactional(readOnly = true)
    public SubReditDto getSubreddit(Long id) {
        Subreddit subreddit = subReditRepository.findById(id)
                .orElseThrow(() -> new SubReditNotFoundException("Subreddit not found with id -" + id));
        return mapToDto(subreddit);
    }
    
	private SubReditDto mapToDto(Subreddit subreddit) {
		return SubReditDto.builder().name(subreddit.getName())
				.description(subreddit.getDescription())
				.build();
 
	}
	
    private Subreddit mapToSubreddit(SubReditDto subredditDto) {
        return Subreddit.builder().name("/r/" + subredditDto.getName())
                .description(subredditDto.getDescription())
                .user(authService.getCurrentUser())
                .createdDate(Instant.now()).build();
    }
}
