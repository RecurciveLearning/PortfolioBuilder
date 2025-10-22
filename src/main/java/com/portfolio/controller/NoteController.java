package com.portfolio.controller;
import com.portfolio.entity.Note;
import com.portfolio.entity.UserProfile;
import com.portfolio.service.NoteService;
import com.portfolio.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final PortfolioService portfolioService;

    public NoteController(NoteService noteService, PortfolioService portfolioService) {
        this.noteService = noteService;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/notepad")
    public String openNotepad(Model model) {
        UserProfile profile = portfolioService.getUserProfile();
        model.addAttribute("profile", profile);
        return "notepad";
    }

    @PostMapping(value = "/save", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Note> save(@RequestBody Note note) {
        Note savedNote = noteService.save(note);
        return ResponseEntity.ok(savedNote);
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Note>> getAll() {
        return ResponseEntity.ok(noteService.getAll());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Note> getById(@PathVariable Long id) {
        Note note = noteService.getById(id);
        return note != null ? ResponseEntity.ok(note) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

