package ru.test.note.service;

import ru.test.note.exception.NoteNotFoundException;
import ru.test.note.model.Note;
import ru.test.note.model.NoteRequest;
import ru.test.note.model.NoteUpdateRequest;
import ru.test.note.repository.NoteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoteService {
    private final NoteRepository repository;

    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }
    public Note create(NoteRequest request) {
        Note note = Note.create(request.title(), request.content(), request.tags());
        log.info("Creating note: {}", note.title());
        return repository.save(note);
    }

    public Note findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    public Note update(String id, NoteUpdateRequest request) {
        Note existing = findById(id);

        String newTitle = request.title() != null ? request.title() : existing.title();
        String newContent = request.content() != null ? request.content() : existing.content();
        var newTags = request.tags() != null ? request.tags() : existing.tags();

        Note updated = new Note(existing.id(), newTitle, newContent, existing.createdAt(), newTags);
        log.info("Updated note: {}", id);
        return repository.save(updated);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            log.warn("Attempt to delete non-existing note: {}", id);
            throw new NoteNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Note deleted: {}", id);
    }

    public List<Note> findAll() {
        return repository.findAll();
    }

    public List<Note> findByTag(String tag) {
        return repository.findByTag(tag);
    }
}
