package ru.test.note.exception;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String id) {
        super("Note not found: " + id);
    }
}

