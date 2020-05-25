package com.learning.redit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.redit.modal.Comment;
import com.learning.redit.modal.Post;
import com.learning.redit.modal.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
