package com.learning.redit.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.redit.dto.SubReditDto;
import com.learning.redit.service.SubReditService;

@RestController
@RequestMapping("/api/subreddit")
public class SubReditController {
	
	@Autowired
	private SubReditService subreditService;
	
	 @GetMapping
	    public List<SubReditDto> getAllSubreddits() {
	        return subreditService.getAll();
	    }

	    @GetMapping("/{id}")
	    public SubReditDto getSubreddit(@PathVariable Long id) {
	        return subreditService.getSubreddit(id);
	    }

	    @PostMapping
	    public SubReditDto create(@RequestBody @Valid SubReditDto subredditDto) {
	        return subreditService.save(subredditDto);
	    }
	
}
