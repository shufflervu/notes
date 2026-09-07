package ru.test.note.model;

import jakarta.validation.constraints.Size;
import java.util.Set;

public record NoteUpdateRequest(
        @Size(max = 200)
        String title,

        @Size(max = 10000)
        String content,

        Set<String> tags
) {}

