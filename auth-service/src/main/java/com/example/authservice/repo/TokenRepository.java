package com.example.authservice.repo;

import com.example.authservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = """
            select t from Token t inner join User u
            on t.userId = u.id
            where u.id = :userId and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(Long userId);

    Optional<Token> findByToken(String token);
    @Query(value = "SELECT u FROM Token u WHERE u.userId =:userId")
    Optional<Token> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Token u WHERE u.userId = :userId")
    void deleteTokenById(Long userId);
}
