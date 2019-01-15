package de.djuelg.vostore.domain.interactors.preview.impl;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.preview.DeleteNoteInteractor;
import de.djuelg.vostore.domain.model.preview.Note;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 11.07.17.
 */

public class DeleteNoteInteractorImpl extends AbstractInteractor implements DeleteNoteInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String uuid;

    public DeleteNoteInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String uuid) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        final Optional<Note> deletedItem = repository.note().get(uuid);
        if (deletedItem.isPresent()) {
            repository.note().delete(deletedItem.get());

            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onNoteDeleted(deletedItem.get());
                }
            });
        }
    }
}
