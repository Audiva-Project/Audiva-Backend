package com.example.audiva.repository;

import com.example.audiva.entity.UserPremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPremiumRepository extends JpaRepository<UserPremium, Long> {
    Optional<UserPremium> findByPaymentRef(String paymentRef);

    @Query("SELECT up FROM UserPremium up WHERE up.user.id = ?1 AND up.premium.name = ?2")
    Optional<UserPremium> findByUserIdAndPremiumName(String userId, String premiumName);

    Optional<UserPremium> findByUserId(String userId);
}
