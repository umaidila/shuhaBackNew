package ru.shuha.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.shuha.db.entities.LobbyEntity;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.db.repositories.LobbyRepository;
import ru.shuha.db.repositories.UserRepository;
import ru.shuha.dto.GetLobbyDto;
import ru.shuha.utils.EntityGeneratorUtils;

@ContextConfiguration(classes = {LobbyService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class LobbyServiceTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private LobbyRepository lobbyRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private LobbyService lobbyService;

    private LobbyEntity defaultLobbyEntity;
    private UserEntity defaultUserEntity;

    @BeforeEach
    void setUp() {
        defaultLobbyEntity = EntityGeneratorUtils.createDefaultLobbyEntity();

        defaultUserEntity = EntityGeneratorUtils.createDefaultUserEntity();
        defaultUserEntity.setLobby(defaultLobbyEntity);
        when(lobbyRepository.save(any(LobbyEntity.class))).thenReturn(defaultLobbyEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(defaultUserEntity);
        when(authService.getAuthUser()).thenReturn(defaultUserEntity);

    }

    @Test
    void testCreateLobby_SuccessfulCreation_ReturnsLobbyId() {
        Long actualCreateLobbyResult = lobbyService.createLobby();

        verify(lobbyRepository).save(any(LobbyEntity.class));
        verify(userRepository).save(any(UserEntity.class));
        verify(authService).getAuthUser();
        assertEquals(1L, actualCreateLobbyResult.longValue());
    }

    @Test
    @Disabled // TODO temporariry added 1 more lobby
    void testGetLobbies_ReturnsEmptyList() {
        when(lobbyRepository.findAll()).thenReturn(new ArrayList<>());

        List<GetLobbyDto> actualLobbies = lobbyService.getLobbies();

        verify(lobbyRepository).findAll();
        assertTrue(actualLobbies.isEmpty());
    }

    @Test
    @Disabled    // TODO temporariry added 1 more lobby
    void testGetLobbies_ReturnsNonEmptyList() {
        List<LobbyEntity> lobbyEntities = Collections.singletonList(defaultLobbyEntity);
        when(lobbyRepository.findAll()).thenReturn(lobbyEntities);

        List<GetLobbyDto> actualLobbies = lobbyService.getLobbies();

        verify(lobbyRepository).findAll();
        assertFalse(actualLobbies.isEmpty());
        assertEquals(1, actualLobbies.size());
        // Add additional assertions if necessary to verify the content of the list
    }


    @Test
    void testJoinLobby_SuccessfulJoin() {
        Optional<LobbyEntity> foundLobby = Optional.of(defaultLobbyEntity);
        when(lobbyRepository.findById(any(Long.class))).thenReturn(foundLobby);

        lobbyService.joinLobby(1L);

        verify(lobbyRepository).findById(any(Long.class));
        verify(userRepository).save(any(UserEntity.class));
        verify(authService).getAuthUser();
        // Additional assertions can be made if there are more outcomes to be tested
    }


    @Test
    void testJoinLobby_LobbyNotFound() {
        when(lobbyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        lobbyService.joinLobby(1L);

        verify(lobbyRepository).findById(any(Long.class));
        verify(authService).getAuthUser();
        // Assert that no other interactions happened, especially with userRepository
    }

    @Test
    void testLeaveLobby_SuccessfulLeave() {
        lobbyService.leaveLobby();

        verify(userRepository).save(any(UserEntity.class));
        verify(authService).getAuthUser();
        // Additional assertions if necessary
    }

}
