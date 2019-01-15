package de.djuelg.vostore.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.collections.Lists;
import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.todolist.DisplayTodoListInteractor;
import de.djuelg.vostore.domain.model.preview.Importance;
import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.domain.model.todolist.TodoListSection;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayTodoListInteractorImpl extends AbstractInteractor implements DisplayTodoListInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String uuid;

    public DisplayTodoListInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                         Callback callback, Repository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    private void postTodoList(final Iterable<TodoListSection> sections) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onTodoListRetrieved(Lists.newArrayList(sections));
            }
        });
    }

    @Override
    public void run() {
        Optional<TodoList> todoList = repository.todoList().getTodoListById(uuid);
        if (todoList.isPresent()) {
            Iterable<TodoListSection> sections = repository.todoList().getSectionsOfTodoListId(uuid);
            postTodoList(sections);

            Importance.increase(repository, todoList.get());
            Importance.checkForNormalization(repository, todoList.get());
        } else {
            callback.onInvalidTodoListUuid();
        }
    }
}
