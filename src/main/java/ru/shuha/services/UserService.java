package ru.shuha.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.db.repositories.UserRepository;
import ru.shuha.dto.RegisterUserDto;
import ru.shuha.enums.UserRole;
import ru.shuha.exceptions.ElementAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
}
