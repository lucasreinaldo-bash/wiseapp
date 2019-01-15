package de.djuelg.vostore.domain.interactors.preview.impl;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.preview.AddTodoListInteractor;
import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddTodoListInteractorImpl extends AbstractInteractor implements AddTodoListInteractor {

    private final AddTodoListInteractorImpl.Callback callback;
    private final Repository repository;
    private final String title;

    public AddTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String title) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.title = title;
    }

    @Override
    public void run() {
        final int position = repository.preview().count();
        // try to insert with new UUID on failure
        TodoList item = new TodoList(title, position);
        while(!repository.todoList().insert(item)) {
            item = new TodoList(title, position);
        }

        final String uuid = item.getUuid();
        final String title = item.getTitle();

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onTodoListAdded(uuid, title);
            }
        });
    }
}
