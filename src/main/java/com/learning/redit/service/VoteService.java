package com.learning.redit.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.redit.dto.VoteDto;
import com.learning.redit.exception.PostNotFoundException;
import com.learning.redit.exception.RedditException;
import com.learning.redit.modal.Post;
import com.learning.redit.modal.Vote;
import com.learning.redit.modal.VoteType;
import com.learning.redit.repository.PostRepository;
import com.learning.redit.repository.VoteRepository;

@Service
public class VoteService {
	
	@Autowired
	private VoteRepository voteRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private AuthServcie authService;
	

	public void vote(VoteDto voteDto) {
		 Post post = postRepository.findById(voteDto.getPostId())
	                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
		 Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
	        if (voteByPostAndUser.isPresent() &&
	                voteByPostAndUser.get().getVoteType()
	                        .equals(voteDto.getVoteType())) {
	            throw new RedditException("You have already "
	                    + voteDto.getVoteType() + "'d for this post");
	        }
	        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
	            post.setVoteCount(post.getVoteCount() + 1);
	        } else {
	            post.setVoteCount(post.getVoteCount() - 1);
	        }
	        voteRepository.save(mapToVote(voteDto, post));
	        postRepository.save(post);
	    }

	    private Vote mapToVote(VoteDto voteDto, Post post) {
	        return Vote.builder()
	                .voteType(voteDto.getVoteType())
	                .post(post)
	                .user(authService.getCurrentUser())
	                .build();
	    }

}
