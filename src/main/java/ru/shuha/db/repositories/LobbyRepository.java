package ru.shuha.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shuha.db.entities.LobbyEntity;

public interface LobbyRepository extends JpaRepository<LobbyEntity, Long> {
}
