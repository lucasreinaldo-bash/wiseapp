package de.djuelg.vostore.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import java.util.InputMismatchException;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.domain.model.todolist.TodoListHeader;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditHeaderInteractorImpl extends AbstractInteractor implements EditHeaderInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String uuid;
    private final String title;
    private final int position;
    private final boolean expanded;

    public EditHeaderInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String uuid, String title, int position, boolean expanded) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.title = title;
        this.position = position;
        this.expanded = expanded;
    }

    @Override
    public void run() {
        final Optional<TodoListHeader> outDatedItem = repository.todoList().getHeaderById(uuid);
        if (!outDatedItem.isPresent()) {
            throw new InputMismatchException("Item not existing!");
        }

        final TodoListHeader updatedItem = outDatedItem.get().update(title, position, expanded);
        repository.todoList().update(updatedItem);

        final Optional<TodoList> todoList = repository.todoList().getTodoListById(updatedItem.getParentTodoListUuid());
        final TodoListHeader itemFromUI = new TodoListHeader(uuid, title, outDatedItem.get().getCreatedAt(), outDatedItem.get().getChangedAt(),
                position, expanded, outDatedItem.get().getParentTodoListUuid());
        if (todoList.isPresent() && !outDatedItem.get().equals(itemFromUI)) repository.todoList().update(todoList.get().updateLastChange());

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onHeaderUpdated(updatedItem);
            }
        });
    }
}
