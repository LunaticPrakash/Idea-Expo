package com.ideaexpobackend.idea_expo_backend.repositories;

import com.ideaexpobackend.idea_expo_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
