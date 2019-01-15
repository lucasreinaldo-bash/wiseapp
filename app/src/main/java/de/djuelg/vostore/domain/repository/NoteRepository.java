package de.djuelg.vostore.domain.repository;

import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

import de.djuelg.vostore.domain.model.preview.Note;

/**
 * A repository that is responsible for the single note pages
 */
public interface NoteRepository {

    List<Note> getAll();

    Optional<Note> get(String uuid);

    boolean insert(Note note);

    void update(Note updatedNote);

    void delete(Note deletedNote);
}
