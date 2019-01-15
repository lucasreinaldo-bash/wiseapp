package de.djuelg.vostore.domain.interactors.todolist.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.todolist.DisplayItemInteractor;
import de.djuelg.vostore.domain.model.todolist.TodoListItem;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayItemInteractorImpl extends AbstractInteractor implements DisplayItemInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String itemUuid;

    public DisplayItemInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     Callback callback, Repository repository, String itemUuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.itemUuid = itemUuid;
    }

    private void postHeader(final TodoListItem item) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onItemRetrieved(item);
            }
        });
    }

    @Override
    public void run() {
        Optional<TodoListItem> item = repository.todoList().getItemById(itemUuid);
        if (item.isPresent()) postHeader(item.get());
    }
}
