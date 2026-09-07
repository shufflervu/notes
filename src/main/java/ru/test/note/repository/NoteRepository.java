package ru.test.note.repository;

import ru.test.note.model.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository {
    Note save(Note note);
    Optional<Note> findById(String id);
    List<Note> findAll();
    List<Note> findByTag(String tag);
    void deleteById(String id);
    boolean existsById(String id);
}
