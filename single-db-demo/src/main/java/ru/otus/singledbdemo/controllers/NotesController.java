package ru.otus.singledbdemo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.singledbdemo.dto.SaveNoteRequestDto;
import ru.otus.singledbdemo.services.NotesService;

@RequiredArgsConstructor
@Controller
public class NotesController {

    private final NotesService notesService;

    @GetMapping("/")
    public String getNotesListPage(Model model) {
        model.addAttribute("notes", notesService.findAllNotes());
        model.addAttribute("users", notesService.findUsers());
        return "notesList";
    }

    @PostMapping("/")
    public String saveNewNote(SaveNoteRequestDto requestDto) {
        notesService.saveNote(requestDto.toDomainObject());
        return "redirect:/";
    }
}
