package de.djuelg.vostore.presentation.presenters.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.note.DisplayNoteInteractor;
import de.djuelg.vostore.domain.interactors.note.EditNoteBodyInteractor;
import de.djuelg.vostore.domain.interactors.note.impl.DisplayNoteInteractorImpl;
import de.djuelg.vostore.domain.interactors.note.impl.EditNoteBodyInteractorImpl;
import de.djuelg.vostore.domain.model.preview.Note;
import de.djuelg.vostore.domain.repository.Repository;
import de.djuelg.vostore.presentation.presenters.DisplayNotePresenter;
import de.djuelg.vostore.presentation.presenters.base.AbstractPresenter;

/**
 * Created by djuelg on 16.07.17.
 */

public class DisplayNotePresenterImpl extends AbstractPresenter implements DisplayNotePresenter, DisplayNoteInteractor.Callback, EditNoteBodyInteractor.Callback {

    private View mView;
    private Repository repository;

    public DisplayNotePresenterImpl(Executor executor, MainThread mainThread,
                                    View view, Repository noteRepository) {
        super(executor, mainThread);
        mView = view;
        repository = noteRepository;
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
    public void loadNote(String uuid) {
        // initialize the interactor
        DisplayNoteInteractor interactor = new DisplayNoteInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid
        );

        // run the interactor
        interactor.execute();
    }

    @Override
    public void editNote(String uuid, String body) {
        EditNoteBodyInteractor interactor = new EditNoteBodyInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                repository,
                uuid,
                body
        );

        interactor.execute();
    }

    @Override
    public void onNoteRetrieved(Optional<Note> note) {
        mView.onNoteLoaded(note);
    }

    @Override
    public void onNoteUpdated(Note updatedNote) {
        // Nothing to do
    }
}
