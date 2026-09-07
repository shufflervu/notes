package ru.test.note.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record Note(
        String id,
        String title,
        String content,
        LocalDateTime createdAt,
        Set<String> tags
) {
    public static Note create(String title, String content, Set<String> tags) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        return new Note(id, title, content, createdAt, tags);
    }
}

