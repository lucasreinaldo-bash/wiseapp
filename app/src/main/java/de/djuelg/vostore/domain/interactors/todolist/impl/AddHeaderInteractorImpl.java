package de.djuelg.vostore.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.todolist.AddHeaderInteractor;
import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.domain.model.todolist.TodoListHeader;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddHeaderInteractorImpl extends AbstractInteractor implements AddHeaderInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String title;
    private final String parentTodoListUuid;

    public AddHeaderInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String title, String parentTodoListUuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.title = title;
        this.parentTodoListUuid = parentTodoListUuid;
    }

    @Override
    public void run() {
        final Optional<TodoList> todoList = repository.todoList().getTodoListById(parentTodoListUuid);
        final int position = repository.todoList().getHeaderCountOfTodoList(parentTodoListUuid);
        if (!todoList.isPresent()) {
            callback.onParentNotFound();
            return;
        }

        // try to insert with new UUID on failure
        TodoListHeader header = new TodoListHeader(title, position, parentTodoListUuid);
        while(!repository.todoList().insert(header)) {
            header = new TodoListHeader(title, position, parentTodoListUuid);
        }

        repository.todoList().update(todoList.get().updateLastChange());

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onHeaderAdded();
            }
        });
    }
}
