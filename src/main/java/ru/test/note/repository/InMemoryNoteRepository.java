package ru.test.note.repository;

import org.apache.catalina.util.StringUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.test.note.model.Note;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryNoteRepository implements NoteRepository {
    private final Map<String, Note> store = new ConcurrentHashMap<>();

    @Override
    public Note save(Note note) {
        store.put(note.id(), note);
        return note;
    }

    @Override
    public Optional<Note> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Note> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Note> findByTag(String tag) {
        if (Strings.isBlank(tag)) return findAll();
        return store.values().stream()
                .filter(n -> n.tags().contains(tag))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        store.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return store.containsKey(id);
    }
}