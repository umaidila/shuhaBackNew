package ru.shuha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shuha.enums.LobbyType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetLobbyDto {

    private Long id;
    private LobbyType type;
    private String creationDate;
    private int size;
}

