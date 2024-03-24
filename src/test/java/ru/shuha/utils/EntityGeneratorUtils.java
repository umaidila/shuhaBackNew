package ru.shuha.utils;

import ru.shuha.db.entities.LobbyEntity;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.enums.LobbyType;
import ru.shuha.enums.UserRole;

import java.time.LocalDate;
import java.util.HashSet;

public class EntityGeneratorUtils {

    public static LobbyEntity createDefaultLobbyEntity(){
        LobbyEntity defaultLobbyEntity = new LobbyEntity();
        defaultLobbyEntity.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        defaultLobbyEntity.setId(1L);
        defaultLobbyEntity.setKind(LobbyType.PUBLIC);
        defaultLobbyEntity.setUsers(new HashSet<>());
        return defaultLobbyEntity;
    }

    public static UserEntity createDefaultUserEntity(){
        UserEntity defaultUserEntity = new UserEntity();
        defaultUserEntity.setId(1L);
        defaultUserEntity.setLogin("Login");
        defaultUserEntity.setPassword("iloveyou");
        defaultUserEntity.setRole(UserRole.ROLE_USER);
        return defaultUserEntity;
    }

}
