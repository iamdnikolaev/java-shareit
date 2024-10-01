package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String error;
}