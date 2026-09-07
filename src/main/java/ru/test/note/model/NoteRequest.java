package ru.test.note.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record NoteRequest(
        @NotBlank(message = "title must not be blank")
        @Size(max = 200)
        String title,

        @Size(max = 10000)
        String content,

        Set<String> tags
) {}

