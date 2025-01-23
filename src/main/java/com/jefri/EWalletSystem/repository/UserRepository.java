package com.jefri.EWalletSystem.repository;

import com.jefri.EWalletSystem.entity.Transaction;
import com.jefri.EWalletSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
