package ru.shuha.db.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import ru.shuha.enums.LobbyType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lobby", schema = "public")
public class LobbyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::lobby_type")
    private LobbyType kind;

    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "lobby")
    private Set<UserEntity> users = new HashSet<>();
}
