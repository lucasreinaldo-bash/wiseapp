package de.djuelg.vostore.domain.interactors.preview.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.preview.DeleteTodoListInteractor;
import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 11.07.17.
 */

public class DeleteTodoListInteractorImpl extends AbstractInteractor implements DeleteTodoListInteractor {

    private final DeleteTodoListInteractor.Callback callback;
    private final Repository repository;
    private final String uuid;

    public DeleteTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        final Optional<TodoList> deletedItem = repository.todoList().getTodoListById(uuid);
        if (deletedItem.isPresent()) {
            repository.todoList().delete(deletedItem.get());

            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onTodoListDeleted(deletedItem.get());
                }
            });
        }
    }
}
