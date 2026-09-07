package ru.test.note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.test.note.exception.NoteNotFoundException;
import ru.test.note.model.Note;
import ru.test.note.model.NoteRequest;
import ru.test.note.model.NoteUpdateRequest;
import ru.test.note.repository.NoteRepository;
import ru.test.note.service.NoteService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotesServiceApplicationTests {

    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    private String noteId = "abc-123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAndReturnNote() {
        var request = new NoteRequest("Title", "Content", Set.of("tag1"));
        var expectedNote = Note.create("Title", "Content", Set.of("tag1"));

        when(noteRepository.save(any())).thenReturn(expectedNote);

        var result = noteService.create(request);

        assertEquals(expectedNote.title(), result.title());
        assertEquals(expectedNote.content(), result.content());
        verify(noteRepository).save(any());
    }

    @Test
    void findById_shouldReturnNote() {
        var note = new Note(noteId, "Title", "Content", null, Set.of());
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        var result = noteService.findById(noteId);

        assertEquals(noteId, result.id());
        verify(noteRepository).findById(noteId);
    }

    @Test
    void findById_whenNotFound_shouldThrowException() {
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.findById(noteId));
        verify(noteRepository).findById(noteId);
    }

    @Test
    void update_shouldCreateNewRecordAndSave() {
        var existing = new Note(noteId, "Old", "Old content", null, Set.of("old"));
        var request = new NoteUpdateRequest("New title", null, null);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existing));
        when(noteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var updated = noteService.update(noteId, request);

        assertEquals("New title", updated.title());
        assertEquals("Old content", updated.content());
        assertEquals(Set.of("old"), updated.tags());
        verify(noteRepository).save(any());
    }

    @Test
    void delete_whenExists_shouldDelete() {
        when(noteRepository.existsById(noteId)).thenReturn(true);

        noteService.delete(noteId);

        verify(noteRepository).deleteById(noteId);
    }

    @Test
    void delete_whenNotExists_shouldThrowException() {
        when(noteRepository.existsById(noteId)).thenReturn(false);

        NoteNotFoundException thrown = assertThrows(NoteNotFoundException.class, () -> noteService.delete(noteId));
        assertTrue(thrown.getMessage().contains(noteId));
        verify(noteRepository).existsById(noteId);
        verify(noteRepository, never()).deleteById(any());
    }
}
