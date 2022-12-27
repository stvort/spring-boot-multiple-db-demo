package ru.otus.singledbdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.singledbdemo.domain.Note;
import ru.otus.singledbdemo.domain.User;

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
}
