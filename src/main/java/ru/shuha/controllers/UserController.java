package ru.shuha.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shuha.dto.LoginResponseDto;
import ru.shuha.dto.LoginUserDto;
import ru.shuha.dto.RegisterUserDto;
import ru.shuha.dto.TokenDto;
import ru.shuha.services.AuthService;
import ru.shuha.services.TokenService;
import ru.shuha.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<Long> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto){
        Long id = userService.registerUser(registerUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginUserDto loginUserDto){
        return ResponseEntity.ok(userService.loginUser(loginUserDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody TokenDto requestDto){
        String token = tokenService.updateTokenByAccess(requestDto.getToken());
        return ResponseEntity.ok(new TokenDto(token));
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getUserLogin(){
        return ResponseEntity.ok(authService.getAuthLogin());
    }
}
