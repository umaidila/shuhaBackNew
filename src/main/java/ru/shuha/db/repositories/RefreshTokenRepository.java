package ru.shuha.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shuha.db.entities.RefreshTokenEntity;
import ru.shuha.db.entities.UserEntity;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    void deleteAllByUser(UserEntity user);
    boolean existsByToken(String token);
    Optional<RefreshTokenEntity> findByToken(String token); // <--->
}
