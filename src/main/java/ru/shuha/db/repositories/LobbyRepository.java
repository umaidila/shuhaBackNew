package ru.shuha.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shuha.db.entities.LobbyEntity;

@Repository
public interface LobbyRepository extends JpaRepository<LobbyEntity, Long> {
}
