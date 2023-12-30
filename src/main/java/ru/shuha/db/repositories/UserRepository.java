package ru.shuha.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shuha.db.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    boolean existsByLogin(String login);
}
