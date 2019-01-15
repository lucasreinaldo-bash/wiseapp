package de.djuelg.vostore.domain.interactors.preview.impl;

import com.fernandocejas.arrow.collections.Lists;

import de.djuelg.vostore.domain.executor.Executor;
import de.djuelg.vostore.domain.executor.MainThread;
import de.djuelg.vostore.domain.interactors.base.AbstractInteractor;
import de.djuelg.vostore.domain.interactors.preview.DisplayPreviewInteractor;
import de.djuelg.vostore.domain.model.preview.ItemsPerPreview;
import de.djuelg.vostore.domain.model.preview.Preview;
import de.djuelg.vostore.domain.repository.Repository;

/**
 * Created by djuelg on 09.07.17.
 */
public class DisplayPreviewInteractorImpl extends AbstractInteractor implements DisplayPreviewInteractor {

    private final static int MAX_DISPLAYED_ITEMS = 4;

    private final DisplayPreviewInteractor.Callback callback;
    private final Repository repository;

    public DisplayPreviewInteractorImpl(Executor threadExecutor,
                                        MainThread mainThread,
                                        Callback callback, Repository repository) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.repository = repository;
    }

    private void postPreviews(final Iterable<Preview> previews) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onPreviewsRetrieved(Lists.newArrayList(previews));
            }
        });
    }

    @Override
    public void run() {
        Iterable<Preview> previews = repository.preview().getAll(new ItemsPerPreview(MAX_DISPLAYED_ITEMS));
        postPreviews(previews);
    }
}
