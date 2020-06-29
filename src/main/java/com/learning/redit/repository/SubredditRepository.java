package com.learning.redit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.redit.modal.Subreddit;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subredditName);
}