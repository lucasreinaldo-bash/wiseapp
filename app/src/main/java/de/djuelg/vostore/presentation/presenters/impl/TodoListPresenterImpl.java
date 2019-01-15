package de.djuelg.vostore.presentation.presenters.impl;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.preview.AddTodoListInteractor;
import de.djuelg.vostore.domain.interactors.preview.EditTodoListInteractor;
import de.djuelg.vostore.domain.interactors.preview.impl.AddTodoListInteractorImpl;
import de.djuelg.vostore.domain.interactors.preview.impl.EditTodoListInteractorImpl;
import de.djuelg.vostore.domain.model.preview.TodoList;
import de.djuelg.vostore.domain.repository.Repository;
import de.djuelg.vostore.presentation.presenters.TodoListPresenter;
import de.djuelg.vostore.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class TodoListPresenterImpl extends AbstractPresenter implements TodoListPresenter, AddTodoListInteractor.Callback, EditTodoListInteractor.Callback {

    private TodoListPresenter.View mView;
    private Repository repository;

    public TodoListPresenterImpl(Executor executor, MainThread mainThread,
                                 View view, Repository repository) {
        super(executor, mainThread);
        mView = view;
        this.repository = repository;
    }

    @Override
    public void resume() {
        // Nothing to do
    }

    @Override
    public void pause() {
        // Nothing to do
    }

    @Override
    public void stop() {
        // Nothing to do
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

    @Override
    public void addTodoList(String title) {
        // initialize the interactor
        AddTodoListInteractor interactor = new AddTodoListInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                title
        );

        // run the interactor
        interactor.execute();
    }

    @Override
    public void editTodoList(String uuid, String title, int position) {
        EditTodoListInteractor interactor = new EditTodoListInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid,
                title,
                position
        );

        interactor.execute();
    }

    @Override
    public void onTodoListAdded(String uuid, String title) {
        mView.onTodoListAdded(uuid, title);
    }

    @Override
    public void onTodoListUpdated(TodoList updatedTodoList) {
        mView.onTodoListEdited(updatedTodoList.getUuid(), updatedTodoList.getTitle());
    }
}
