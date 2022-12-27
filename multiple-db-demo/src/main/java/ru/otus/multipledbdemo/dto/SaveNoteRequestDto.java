package ru.otus.multipledbdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.multipledbdemo.domain.primary.Note;
import ru.otus.multipledbdemo.domain.primary.User;

@Data
@AllArgsConstructor
public class SaveNoteRequestDto {
    private String userName;

    private String userLogin;

    private String title;

    private String text;

    public Note toDomainObject(){
        return new Note(0L, new User(0L, userName, userLogin), title, text);
    }

    public static SaveNoteRequestDto fromDomainObject(Note note){
        return new SaveNoteRequestDto(note.getUser().getName(),
                note.getUser().getLogin(), note.getTitle(), note.getText());
    }
}
