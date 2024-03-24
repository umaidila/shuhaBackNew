package ru.shuha.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.db.repositories.UserRepository;
import ru.shuha.dto.LoginResponseDto;
import ru.shuha.dto.LoginUserDto;
import ru.shuha.dto.RegisterUserDto;
import ru.shuha.enums.UserRole;
import ru.shuha.exceptions.ElementAlreadyExistsException;
import ru.shuha.exceptions.ElementNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    @Transactional
    public Long registerUser(RegisterUserDto registerUserDto) {
        log.info("Registering new user {}", registerUserDto);
        if (userRepository.existsByLogin(registerUserDto.getEmail())){
            throw new ElementAlreadyExistsException("User with email " + registerUserDto.getEmail() + " already exists");
        }
        UserEntity newUser = new UserEntity();
        newUser.setRole(UserRole.ROLE_USER);
        newUser.setPassword(bCryptPasswordEncoder.encode(registerUserDto.getPassword()));
        newUser.setLogin(registerUserDto.getEmail());

        return userRepository.save(newUser).getId();
    }

    @Transactional
    public LoginResponseDto loginUser(LoginUserDto loginUserDto){
        log.info("Login user {}", loginUserDto);
        UserEntity existedUser = userRepository.findByLogin(loginUserDto.getEmail()).orElseThrow(
                () -> new ElementNotFoundException("User not found")
        );
        if (!bCryptPasswordEncoder.matches(loginUserDto.getPassword(), existedUser.getPassword())) {
            throw new ElementNotFoundException("Wrong password");
        }
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setAccessToken(tokenService.generateAccessToken(existedUser.getLogin()));
        responseDto.setRefreshToken(tokenService.generateRefreshToken(existedUser));
        return responseDto;
    }
}
