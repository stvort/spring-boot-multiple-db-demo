package ru.otus.multipledbdemo.repositories.primary;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.multipledbdemo.domain.primary.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
