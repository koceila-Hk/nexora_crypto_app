package com.nexora.nexora_crypto_api.repository;

import com.nexora.nexora_crypto_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPseudonym(String pseudonym);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
}
