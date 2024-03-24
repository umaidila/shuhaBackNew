package ru.shuha.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.shuha.db.entities.LobbyEntity;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.db.repositories.UserRepository;
import ru.shuha.dto.LoginUserDto;
import ru.shuha.dto.RegisterUserDto;
import ru.shuha.enums.LobbyType;
import ru.shuha.enums.UserRole;
import ru.shuha.exceptions.ElementAlreadyExistsException;
import ru.shuha.exceptions.ElementNotFoundException;
import ru.shuha.utils.EntityGeneratorUtils;

@ContextConfiguration(classes = {UserService.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceTest {
    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private UserEntity defaultUserEntity;
    private LobbyEntity defaultLobbyEntity;

    @BeforeEach
    void setUp() {
        defaultLobbyEntity = EntityGeneratorUtils.createDefaultLobbyEntity();

        defaultUserEntity = EntityGeneratorUtils.createDefaultUserEntity();
        defaultUserEntity.setLobby(defaultLobbyEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(defaultUserEntity);
        //when(authService.getAuthUser()).thenReturn(defaultUserEntity);

    }

    @Test
    void testRegisterUser_ThrowsElementAlreadyExistsException() {
        when(userRepository.existsByLogin(any(String.class))).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class,
                () -> userService.registerUser(new RegisterUserDto("jane.doe@example.org", "iloveyou")));
        verify(userRepository).existsByLogin(eq("jane.doe@example.org"));
    }

    @Test
    void testRegisterUser_SuccessfulRegistration_ReturnsUserId() {
        when(userRepository.existsByLogin(any(String.class))).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(defaultUserEntity);

        Long actualRegisterUserResult = userService.registerUser(new RegisterUserDto("jane.doe@example.org", "iloveyou"));

        verify(userRepository).save(any(UserEntity.class));
        verify(userRepository).existsByLogin(eq("jane.doe@example.org"));
        assertEquals(1L, actualRegisterUserResult.longValue());
    }


    @Test
    void testLoginUser_ThrowsElementNotFoundException_UserNotFound() {
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class,
                () -> userService.loginUser(new LoginUserDto("jane.doe@example.org", "iloveyou")));
        verify(userRepository).findByLogin(eq("jane.doe@example.org"));
    }


}
