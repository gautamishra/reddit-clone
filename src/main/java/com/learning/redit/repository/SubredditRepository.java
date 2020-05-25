package com.learning.redit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.redit.modal.Subreddit;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    java.util.Optional<Subreddit> findByName(String subredditName);
}