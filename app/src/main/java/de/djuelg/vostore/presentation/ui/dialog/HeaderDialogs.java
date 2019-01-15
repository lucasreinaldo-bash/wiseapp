package de.djuelg.vostore.presentation.ui.dialog;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;

import de.djuelg.vostore.R;
import de.djuelg.vostore.domain.executor.impl.ThreadExecutor;
import de.djuelg.vostore.presentation.presenters.HeaderPresenter;
import de.djuelg.vostore.presentation.presenters.impl.HeaderPresenterImpl;
import de.djuelg.vostore.storage.RepositoryImpl;
import de.djuelg.vostore.threading.MainThreadImpl;

import static de.djuelg.vostore.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.vostore.presentation.ui.dialog.BaseDialogs.getString;
import static de.djuelg.vostore.presentation.ui.dialog.BaseDialogs.showTextInputDialog;
import static de.djuelg.vostore.storage.RepositoryManager.FALLBACK_REALM;

/**
 * Created by djuelg on 26.07.17.
 */

public class HeaderDialogs {

    public static void showAddHeaderDialog(Fragment fragment, final String parentTodoListUuid) {
        final HeaderPresenter presenter = instantiatePresenterWith(fragment);
        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.addHeader(title, parentTodoListUuid);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.add_category), callback);
    }

    public static void showEditHeaderDialog(Fragment fragment, final String uuid, final String oldTitle, final int position, final boolean expanded) {
        final HeaderPresenter presenter = instantiatePresenterWith(fragment);
        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.editHeader(uuid, title, position, expanded);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.edit_category), callback, oldTitle);
    }

    private static HeaderPresenter instantiatePresenterWith(Fragment fragment) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
        return new HeaderPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                (HeaderPresenter.View) fragment,
                new RepositoryImpl(repositoryName)
        );
    }


}
