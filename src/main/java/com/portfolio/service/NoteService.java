package com.portfolio.service;

import com.portfolio.entity.Note;
import com.portfolio.repository.NoteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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
        try {
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

        } catch (Exception e) {
            // Handle any unexpected errors
            // You can return null or throw a custom exception if you want
            return null;
        }
    }

    @Cacheable(value = "allNotes")
    public List<Note> getAll() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            return Collections.emptyList(); // Return empty list on error
        }
    }

    @Cacheable(value = "notes", key = "#id")
    public Note getById(Long id) {
        try {
            return repo.findById(id).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @CacheEvict(value = {"notes", "allNotes"}, allEntries = true)
    public boolean delete(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
