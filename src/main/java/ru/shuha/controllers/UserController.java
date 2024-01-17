package ru.shuha.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shuha.dto.LoginUserDto;
import ru.shuha.dto.RegisterUserDto;
import ru.shuha.services.AuthService;
import ru.shuha.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Long> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto){
        Long id = userService.registerUser(registerUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginUserDto loginUserDto){
        String token = userService.loginUser(loginUserDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getUserLogin(){
        return ResponseEntity.ok(authService.getAuthLogin());
    }
}
