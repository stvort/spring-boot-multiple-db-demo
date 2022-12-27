package ru.otus.multipledbdemo.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.otus.multipledbdemo.config.annotations.PrimaryTransactional;
import ru.otus.multipledbdemo.config.annotations.SecondaryTransactional;
import ru.otus.multipledbdemo.domain.primary.Note;
import ru.otus.multipledbdemo.domain.secondary.ExternalUser;
import ru.otus.multipledbdemo.repositories.primary.NoteRepository;
import ru.otus.multipledbdemo.repositories.secondary.UserRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotesService {
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final UsersMapper usersMapper;
    private final TransactionTemplate primaryTransactionTemplate;
    private final TransactionTemplate secondaryTransactionTemplate;

    public NotesService(UserRepository userRepository, NoteRepository noteRepository, UsersMapper usersMapper,
                        @Qualifier("transactionTemplate") TransactionTemplate primaryTransactionTemplate,
                        @Qualifier("secondaryTransactionTemplate") TransactionTemplate secondaryTransactionTemplate) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.usersMapper = usersMapper;
        this.primaryTransactionTemplate = primaryTransactionTemplate;
        this.secondaryTransactionTemplate = secondaryTransactionTemplate;
    }

    @PrimaryTransactional(readOnly = true)
    public List<Note> findAllNotes() {
        var externalUsers = userRepository.findAll();
        var notes = noteRepository.findAll();
        return enhanceNotesUsersInfo(notes, externalUsers);
    }

    @SecondaryTransactional(readOnly = true)
    public List<ExternalUser> findUsers() {
        return userRepository.findAll();
    }

    @PrimaryTransactional
    public Note saveNote(Note note) {
        return secondaryTransactionTemplate.execute(ts -> {
                    var externalUser = userRepository.findByLogin(note.getUser().getLogin())
                            .orElseGet(() -> userRepository.save(usersMapper.userToExternal(note.getUser())));
                    note.setUser(usersMapper.externalUserToUser(externalUser));

                    var savedNote = noteRepository.save(note);
                    //throw new RuntimeException("Ooops");
                    return savedNote;
                }
        );
    }

    private List<Note> enhanceNotesUsersInfo(List<Note> notes, List<ExternalUser> externalUsers) {
        var userMap = externalUsers.stream()
                .collect(Collectors.toMap(ExternalUser::getId, Function.identity()));
        notes.forEach(note -> usersMapper.fillUserFromExternalUser(note.getUser(), userMap.get(note.getUser().getId())));
        return notes;
    }
}
