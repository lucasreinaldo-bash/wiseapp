package de.djuelg.vostore.presentation.ui.dialog;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;

import de.djuelg.vostore.R;
import de.djuelg.vostore.domain.executor.impl.ThreadExecutor;
import de.djuelg.vostore.presentation.presenters.NotePresenter;
import de.djuelg.vostore.presentation.presenters.impl.NotePresenterImpl;
import de.djuelg.vostore.storage.RepositoryImpl;
import de.djuelg.vostore.threading.MainThreadImpl;

import static de.djuelg.vostore.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.vostore.presentation.ui.dialog.BaseDialogs.getString;
import static de.djuelg.vostore.presentation.ui.dialog.BaseDialogs.showTextInputDialog;
import static de.djuelg.vostore.storage.RepositoryManager.FALLBACK_REALM;

/**
 * Created by djuelg on 26.07.17.
 */

public class NoteDialogs {

    public static void showAddNoteDialog(Fragment fragment) {
        final NotePresenter presenter = instantiatePresenterUsing(fragment);

        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.addNote(title);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.add_note), callback);
    }

    public static void showEditNoteDialog(Fragment fragment, final String uuid, final String oldTitle, final int position) {
        final NotePresenter presenter = instantiatePresenterUsing(fragment);

        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.editNote(uuid, title, position);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.edit_note), callback, oldTitle);
    }

    private static NotePresenter instantiatePresenterUsing(Fragment fragment) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
        return new NotePresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                (NotePresenter.View) fragment,
                new RepositoryImpl(repositoryName));
    }
}
