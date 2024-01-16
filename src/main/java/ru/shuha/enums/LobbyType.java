package ru.shuha.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LobbyType {
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE");

    private final String value;
}
