package de.djuelg.vostore.domain.interactors.preview.impl;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.preview.AddNoteInteractor;
import de.djuelg.vostore.domain.model.preview.Note;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 09.07.17.
 */

public class AddNoteInteractorImpl extends AbstractInteractor implements AddNoteInteractor {

    private final Callback callback;
    private final Repository repository;
    private final String title;

    public AddNoteInteractorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, Repository repository, String title) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
        this.title = title;
    }

    @Override
    public void run() {
        final int position = repository.preview().count();
        // try to insert with new UUID on failure
        Note item = new Note(title, position);
        while(!repository.note().insert(item)) {
            item = new Note(title, position);
        }

        final String uuid = item.getUuid();
        final String title = item.getTitle();

        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onNoteAdded(uuid, title);
            }
        });
    }
}
