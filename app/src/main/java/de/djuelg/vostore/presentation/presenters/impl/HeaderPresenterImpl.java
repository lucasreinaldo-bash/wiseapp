package de.djuelg.vostore.presentation.presenters.impl;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.todolist.AddHeaderInteractor;
import de.djuelg.vostore.domain.interactors.todolist.EditHeaderInteractor;
import de.djuelg.vostore.domain.interactors.todolist.impl.AddHeaderInteractorImpl;
import de.djuelg.vostore.domain.interactors.todolist.impl.EditHeaderInteractorImpl;
import de.djuelg.vostore.domain.model.todolist.TodoListHeader;
import de.djuelg.vostore.domain.repository.Repository;
import de.djuelg.vostore.presentation.exception.ParentNotFoundException;
import de.djuelg.vostore.presentation.presenters.HeaderPresenter;
import de.djuelg.vostore.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class HeaderPresenterImpl extends AbstractPresenter implements HeaderPresenter, AddHeaderInteractor.Callback, EditHeaderInteractor.Callback {

    private View mView;
    private Repository repository;

    public HeaderPresenterImpl(Executor executor, MainThread mainThread,
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
    public void onHeaderAdded() {
        mView.onHeaderAdded();
    }

    @Override
    public void onHeaderUpdated(TodoListHeader updatedHeader) {
        mView.onHeaderEdited();
    }

    @Override
    public void onParentNotFound() {
        throw new ParentNotFoundException("Cannot update header without parent");
    }

    @Override
    public void addHeader(String title, String parentTodoListUuid) {
        // initialize the interactor
        AddHeaderInteractor interactor = new AddHeaderInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                title,
                parentTodoListUuid
        );

        // run the interactor
        interactor.execute();
    }


    @Override
    public void editHeader(String uuid, String title, int position, boolean expanded) {
        EditHeaderInteractor interactor = new EditHeaderInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid,
                title,
                position,
                expanded
        );

        interactor.execute();
    }
}
