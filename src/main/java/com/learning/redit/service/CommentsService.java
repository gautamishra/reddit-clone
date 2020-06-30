package com.learning.redit.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learning.redit.dto.CommentsDto;
import com.learning.redit.exception.PostNotFoundException;
import com.learning.redit.modal.Comment;
import com.learning.redit.modal.NotificationEmail;
import com.learning.redit.modal.Post;
import com.learning.redit.modal.User;
import com.learning.redit.repository.CommentRepository;
import com.learning.redit.repository.PostRepository;
import com.learning.redit.repository.UserRepository;

@Service
public class CommentsService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private AuthServcie authService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public void createComment(CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException("Not found"));
		commentRepository.save(mapToEntity(commentsDto, post));
		
		String message = post.getUser().getUsername() + " posted a comment on your post." + post.getUrl();
		sendNotification(message, post.getUser());
	}
		
	private Comment mapToEntity(CommentsDto commentsDto, Post post) {
		Comment comment = new Comment();
		comment.setCreatedDate(Instant.now());
		comment.setPost(post);
		comment.setText(commentsDto.getText());
		comment.setUser(authService.getCurrentUser());
		return comment;
	}

	public List<CommentsDto> getCommentByPost(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Not Found"));
		return commentRepository.findByPost(post)
				.stream()
				.map(this::commentToDto)
				.collect(Collectors.toList());
	}
	
	private void sendNotification(String message , User user) {
		  mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
	}
	
	private CommentsDto commentToDto(Comment comment) {
		CommentsDto commDto = new CommentsDto();
		commDto.setCreatedDate(comment.getCreatedDate());
		commDto.setId(comment.getId());
		commDto.setPostId(comment.getPost().getPostId());
		commDto.setText(comment.getText());
		commDto.setUserName(comment.getUser().getUsername());
		return commDto;
	}

	public List<CommentsDto> getCommentsByUser(String userName) {
		// TODO Auto-generated method stub
		
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException("Not Found " + userName));
		return commentRepository.findAllByUser(user)
				.stream()
				.map(this::commentToDto)
				.collect(Collectors.toList());
	}
}
