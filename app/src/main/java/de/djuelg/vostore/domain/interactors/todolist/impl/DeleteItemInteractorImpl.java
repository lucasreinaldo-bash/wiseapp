package de.djuelg.vostore.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.todolist.DeleteItemInteractor;
import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.domain.model.todolist.TodoListItem;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 11.07.17.
 */

public class DeleteItemInteractorImpl extends AbstractInteractor implements DeleteItemInteractor{

    private final Callback callback;
    private final Repository repository;
    private final String uuid;

    public DeleteItemInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        final Optional<TodoListItem> deletedItem = repository.todoList().getItemById(uuid);
        if (deletedItem.isPresent()) {
            repository.todoList().delete(deletedItem.get());

            final Optional<TodoList> todoList = repository.todoList().getTodoListById(deletedItem.get().getParentTodoListUuid());
            if (todoList.isPresent()) repository.todoList().update(todoList.get().updateLastChange());

            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onItemDeleted(deletedItem.get());
                }
            });
        }
    }
}
