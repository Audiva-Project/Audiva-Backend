package com.example.audiva.repository;

import com.example.audiva.entity.Premium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PremiumRepository extends JpaRepository<Premium, Long> {
    boolean existsByName(String name);

    Optional<Premium> findByName(String name);

    boolean existsById(Long id);
}
