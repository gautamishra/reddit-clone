package com.learning.redit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.redit.modal.Post;
import com.learning.redit.modal.Subreddit;
import com.learning.redit.modal.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}