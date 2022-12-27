package ru.otus.multipledbdemo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.multipledbdemo.dto.SaveNoteRequestDto;
import ru.otus.multipledbdemo.services.NotesService;

@RequiredArgsConstructor
@Controller
public class NotesController {

    private final NotesService notesService;

    @GetMapping("/")
    public String getNotesListPage(Model model) {
        model.addAttribute("users", notesService.findUsers());
        model.addAttribute("notes", notesService.findAllNotes());
        return "notesList";
    }

    @PostMapping("/")
    public String saveNewNote(SaveNoteRequestDto requestDto) {
        notesService.saveNote(requestDto.toDomainObject());
        return "redirect:/";
    }
}
