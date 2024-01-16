package ru.shuha.services;


import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.db.repositories.UserRepository;
import ru.shuha.dto.LoginUserDto;
import ru.shuha.dto.RegisterUserDto;
import ru.shuha.enums.UserRole;
import ru.shuha.exceptions.ElementAlreadyExistsException;
import ru.shuha.exceptions.ElementNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    @Transactional
    public Long registerUser(RegisterUserDto registerUserDto) {
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
    public String loginUser(LoginUserDto loginUserDto){
        UserEntity existedUser = userRepository.findByLogin(loginUserDto.getEmail()).orElseThrow(
                () -> new ElementNotFoundException("User not found")
        );
        if (!bCryptPasswordEncoder.matches(loginUserDto.getPassword(), existedUser.getPassword())) {
            throw new ElementNotFoundException("Wrong password");
        }
        return tokenService.generateToken(existedUser.getLogin());
    }
}
