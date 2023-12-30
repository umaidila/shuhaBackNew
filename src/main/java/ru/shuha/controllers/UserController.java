package ru.shuha.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shuha.dto.RegisterUserDto;
import ru.shuha.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public ResponseEntity<Long> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto){
        Long id = userService.registerUser(registerUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
