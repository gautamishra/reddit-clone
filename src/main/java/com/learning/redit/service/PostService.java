package com.learning.redit.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.redit.dto.PostRequest;
import com.learning.redit.dto.PostResponse;
import com.learning.redit.exception.PostNotFoundException;
import com.learning.redit.exception.SubReditNotFoundException;
import com.learning.redit.modal.Post;
import com.learning.redit.modal.Subreddit;
import com.learning.redit.modal.User;
import com.learning.redit.repository.PostRepository;
import com.learning.redit.repository.SubredditRepository;
import com.learning.redit.repository.UserRepository;

@Service
public class PostService {
	
	@Autowired
	private SubredditRepository subreditRepositiory;
	
	@Autowired
	private AuthServcie authService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	 public void save(PostRequest postRequest) {
	        Subreddit subreddit = subreditRepositiory.findByName(postRequest.getSubredditName())
	                .orElseThrow(() -> new SubReditNotFoundException(postRequest.getSubredditName()));
	        postRepository.save(mapToPost(postRequest, subreddit, authService.getCurrentUser()));
	 }

	    @Transactional(readOnly = true)
	    public PostResponse getPost(Long id) {
	        Post post = postRepository.findById(id)
	                .orElseThrow(() -> new PostNotFoundException(id.toString()));
	        return mapToDto(post);
	    }

	    @Transactional(readOnly = true)
	    public List<PostResponse> getAllPosts() {
	        return postRepository.findAll()
	                .stream()
	                .map(this::mapToDto)
	                .collect(Collectors.toList());
	    }
	    

	    @Transactional(readOnly = true)
	    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
	        Subreddit subreddit = subreditRepositiory.findById(subredditId)
	                .orElseThrow(() -> new SubReditNotFoundException(subredditId.toString()));
	        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
	        return posts.stream().map(this::mapToDto).collect(Collectors.toList());
	    }

	    
	    @Transactional(readOnly = true)
	    public List<PostResponse> getPostsByUsername(String username) {
	        User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException(username));
	        return postRepository.findByUser(user)
	                .stream()
	                .map(this::mapToDto)
	                .collect(Collectors.toList());
	    }
	
	private Post mapToPost(PostRequest postRequest, Subreddit subRedit , User user) {
		Post post = new Post();
		post.setName(postRequest.getPostName());
		post.setCreatedDate(Instant.now());
		post.setDescription(postRequest.getDescription());
		post.setSubreddit(subRedit);
		post.setUser(user);
		post.setUrl(postRequest.getUrl());
		return post;
	}
	
	private PostResponse mapToDto(Post post) {
		PostResponse postResponse = new PostResponse();
		postResponse.setPostName(post.getName());
		postResponse.setDescription(post.getDescription());
		postResponse.setDuration(post.getCreatedDate().toString());
		postResponse.setUrl(post.getUrl());
		postResponse.setUserName(post.getUser().getUsername());
		postResponse.setSubredditName(post.getSubreddit().getName());
		return postResponse;
	}
}
