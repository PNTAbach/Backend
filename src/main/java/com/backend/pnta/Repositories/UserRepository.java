package com.backend.pnta.Repositories;


import com.backend.pnta.Models.User.Role;
import com.backend.pnta.Models.User.UserP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserP, Long> {
    UserP findByEmail(String userEmail);
    @Query("SELECT u.role, COUNT(u) FROM UserP u GROUP BY u.role")
    List<Object[]> countUsersByRole();
    @Query("SELECT u.role FROM UserP u WHERE u.userId = :userId")
    Optional<Role> getRole(Long userId);
}
