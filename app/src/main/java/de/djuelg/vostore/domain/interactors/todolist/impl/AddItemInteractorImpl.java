package de.djuelg.vostore.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.todolist.AddItemInteractor;
import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.domain.model.todolist.TodoListHeader;
import de.djuelg.vostore.domain.model.todolist.TodoListItem;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddItemInteractorImpl extends AbstractInteractor implements AddItemInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String title;
    private final boolean important;
    private final String details;
    private final String parentTodoListUuid;
    private final String parentHeaderUuid;

    public AddItemInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String title, boolean important, String details, String parentTodoListUuid, String parentHeaderUuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.title = title;
        this.important = important;
        this.details = details;
        this.parentTodoListUuid = parentTodoListUuid;
        this.parentHeaderUuid = parentHeaderUuid;
    }

    @Override
    public void run() {
        final Optional<TodoList> todoList = repository.todoList().getTodoListById(parentTodoListUuid);
        final Optional<TodoListHeader> header = repository.todoList().getHeaderById(parentHeaderUuid);
        final int position = repository.todoList().getSubItemCountOfHeader(parentHeaderUuid);

        if (!todoList.isPresent() || !header.isPresent()) {
            callback.onParentNotFound();
            return;
        }

        // try to insert with new UUID on failure
        TodoListItem item = new TodoListItem(title, position, important, details, parentTodoListUuid, parentHeaderUuid);
        while(!repository.todoList().insert(item)) {
            item = new TodoListItem(title, position, important, details, parentTodoListUuid, parentHeaderUuid);
        }

        repository.todoList().update(todoList.get().updateLastChange());

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onItemAdded();
            }
        });
    }
}
