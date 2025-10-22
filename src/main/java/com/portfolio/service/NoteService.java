package com.portfolio.service;
import com.portfolio.entity.Note;
import com.portfolio.repository.NoteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    @CachePut(value = "notes", key = "#result.id", unless = "#result == null")
    @CacheEvict(value = "allNotes", allEntries = true)
    public Note save(Note note) {
        LocalDateTime now = LocalDateTime.now();

        if (note.getId() != null) {
            // Update existing note
            Optional<Note> existingOpt = repo.findById(note.getId());
            if (existingOpt.isPresent()) {
                Note existing = existingOpt.get();
                existing.setTitle(note.getTitle());
                existing.setContent(note.getContent());
                existing.setUpdatedAt(now);
                return repo.save(existing);
            }
        }

        // New note
        note.setCreatedAt(now);
        note.setUpdatedAt(now);
        return repo.save(note);
    }


    @Cacheable(value = "allNotes")
    public List<Note> getAll() {
        return repo.findAll();
    }


    @Cacheable(value = "notes", key = "#id")
    public Note getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @CacheEvict(value = { "notes", "allNotes" }, allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }
}


