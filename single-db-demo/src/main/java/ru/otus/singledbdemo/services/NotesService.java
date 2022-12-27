package ru.otus.singledbdemo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.singledbdemo.domain.Note;
import ru.otus.singledbdemo.domain.User;
import ru.otus.singledbdemo.repositories.NoteRepository;
import ru.otus.singledbdemo.repositories.UserRepository;

import java.util.List;

@Service
public class NotesService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NotesService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }

    @Transactional
    public Note saveNote(Note note) {
        var noteUser = userRepository.findByLogin(note.getUser().getLogin())
                .orElse(note.getUser());
        note.setUser(noteUser);
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public List<User> findUsers() {
        return userRepository.findAll();
    }
}
