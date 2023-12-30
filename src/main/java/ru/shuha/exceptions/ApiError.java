package ru.shuha.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {

    @Schema(description = "Сообщение", example = "User is not found: id = 123456789")
    private String message;

    @Schema(description = "Код ошибки", example = "404")
    private HttpStatus httpStatus;

    @Schema(description = "Время сервера", example = "2022-07-06T12:12:21.698Z")
    private LocalDateTime timeStamp;

}
