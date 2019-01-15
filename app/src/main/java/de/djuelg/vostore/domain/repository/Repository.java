package de.djuelg.vostore.domain.repository;

/**
 * Created by djuelg on 18.10.17.
 */

public interface Repository {
    PreviewRepository preview();

    TodoListRepository todoList();

    NoteRepository note();
}
