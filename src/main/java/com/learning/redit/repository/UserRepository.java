package com.learning.redit.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.redit.modal.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
