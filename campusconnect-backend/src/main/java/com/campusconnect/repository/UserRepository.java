package com.campusconnect.repository;

import com.campusconnect.model.User;
import com.campusconnect.model.UserRole;
import com.campusconnect.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findByAccountStatus(AccountStatus status);
    boolean existsByEmail(String email);
}