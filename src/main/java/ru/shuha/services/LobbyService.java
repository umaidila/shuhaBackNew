package ru.shuha.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shuha.db.entities.LobbyEntity;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.db.repositories.LobbyRepository;
import ru.shuha.db.repositories.UserRepository;
import ru.shuha.dto.GetLobbyDto;
import ru.shuha.enums.LobbyType;
import ru.shuha.exceptions.PreconditionFailedException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    private static final int MAX_LOBBY_USERS = 10;

    public Long createLobby() {
        UserEntity loggedUser = authService.getAuthUser();
        LobbyEntity newLobby = LobbyEntity.builder()
                .kind(LobbyType.PUBLIC)
                .creationDate(LocalDateTime.now())
                .build();
        LobbyEntity savedNewLobby = lobbyRepository.save(newLobby);
        loggedUser.setLobby(newLobby);
        userRepository.save(loggedUser);
        return savedNewLobby.getId();
    }

    public List<GetLobbyDto> getLobbies() {
        return lobbyRepository.findAll()
                .stream()
                .map(lobby -> GetLobbyDto.builder()
                        .id(lobby.getId())
                        .type(lobby.getKind())
                        .creationDate(lobby.getCreationDate().toString())
                        .size(lobby.getUsers().size())
                        .build())
                .toList();

    }

    public void joinLobby(Long lobbyId) {
        UserEntity loggedUser = authService.getAuthUser();
        lobbyRepository.findById(lobbyId).ifPresent(lobby -> {
            if (lobby.getUsers().size() < MAX_LOBBY_USERS) {
                loggedUser.setLobby(lobby);
                userRepository.save(loggedUser);
            } else {
                throw new PreconditionFailedException("lobby is full");
            }
        });
    }

    public void leaveLobby() {
        UserEntity loggedUser = authService.getAuthUser();
        loggedUser.setLobby(null);
        userRepository.save(loggedUser);
    }

}
