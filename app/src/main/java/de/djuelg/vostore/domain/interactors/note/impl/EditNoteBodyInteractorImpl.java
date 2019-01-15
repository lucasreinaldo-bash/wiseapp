package de.djuelg.vostore.domain.interactors.note.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.note.EditNoteBodyInteractor;
import de.djuelg.vostore.domain.model.preview.Importance;
import de.djuelg.vostore.domain.model.preview.Note;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 10.07.17.
 */

public class EditNoteBodyInteractorImpl extends AbstractInteractor implements EditNoteBodyInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String uuid;
    private final String body;

    public EditNoteBodyInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String uuid, String body) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
        this.body = body;
    }

    @Override
    public void run() {
        final Optional<Note> outDatedItem = repository.note().get(uuid);
        if (outDatedItem.isPresent()) {

            if (!outDatedItem.get().getBody().equals(body)) {
                final Note updatedItem = outDatedItem.get().update(body).updateLastChange();
                // repository.note().update(updatedItem); // not needed because of Importance increasing

                Importance.increase(repository, updatedItem);

                mMainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onNoteUpdated(updatedItem);
                    }
                });
            }
        }
    }
}
