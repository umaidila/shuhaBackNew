package ru.shuha.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shuha.dto.GetLobbyDto;
import ru.shuha.services.LobbyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    @GetMapping
    public ResponseEntity<List<GetLobbyDto>> getLobbies(){
        return ResponseEntity.ok(lobbyService.getLobbies());
    }

    @PostMapping
    public ResponseEntity<Long> createLobby(){
        return ResponseEntity.ok(lobbyService.createLobby());
    }

    @PostMapping("/{lobbyId}/join")
    public ResponseEntity<?> joinLobby(@PathVariable Long lobbyId){
        lobbyService.joinLobby(lobbyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> leaveLobby(){
        lobbyService.leaveLobby();
        return ResponseEntity.ok().build();
    }
}
