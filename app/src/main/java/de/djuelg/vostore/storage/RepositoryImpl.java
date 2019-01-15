package de.djuelg.vostore.storage;

import de.djuelg.vostore.domain.repository.NoteRepository;
import de.djuelg.vostore.domain.repository.PreviewRepository;
import de.djuelg.vostore.domain.repository.Repository;
import de.djuelg.vostore.domain.repository.TodoListRepository;
import io.realm.RealmConfiguration;

import static de.djuelg.vostore.storage.RepositoryManager.createConfiguration;

/**
 * Created by djuelg on 18.10.17.
 */

public class RepositoryImpl implements Repository {

    private final PreviewRepository previewRepository;
    private final TodoListRepository todoListRepository;
    private final NoteRepository noteRepository;

    public RepositoryImpl(String realmName) {
        this(createConfiguration(realmName));
    }

    // RealmConfiguration injectable for testing
    RepositoryImpl(RealmConfiguration configuration) {
        this.previewRepository = new PreviewRepositoryImpl(configuration);
        this.todoListRepository = new TodoListRepositoryImpl(configuration);
        this.noteRepository = new NoteRepositoryImpl(configuration);
    }


    @Override
    public PreviewRepository preview() {
        return previewRepository;
    }

    @Override
    public TodoListRepository todoList() {
        return todoListRepository;
    }

    @Override
    public NoteRepository note() {
        return noteRepository;
    }
}
