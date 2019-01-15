package de.djuelg.vostore.presentation.ui.dialog;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;

import de.djuelg.vostore.R;
import de.djuelg.vostore.domain.executor.impl.ThreadExecutor;
import de.djuelg.vostore.presentation.presenters.TodoListPresenter;
import de.djuelg.vostore.presentation.presenters.impl.TodoListPresenterImpl;
import de.djuelg.vostore.storage.RepositoryImpl;
import de.djuelg.vostore.threading.MainThreadImpl;

import static de.djuelg.vostore.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.vostore.presentation.ui.dialog.BaseDialogs.getString;
import static de.djuelg.vostore.presentation.ui.dialog.BaseDialogs.showTextInputDialog;
import static de.djuelg.vostore.storage.RepositoryManager.FALLBACK_REALM;

/**
 * Created by djuelg on 26.07.17.
 */

public class TodoListDialogs {

    public static void showAddTodoListDialog(Fragment fragment) {
        final TodoListPresenter presenter = instantiatePresenterUsing(fragment);

        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.addTodoList(title);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.add_todo_list), callback);
    }

    public static void showEditTodoListDialog(Fragment fragment, final String uuid, final String oldTitle, final int position) {
        final TodoListPresenter presenter = instantiatePresenterUsing(fragment);

        BaseDialogs.InputDialogCallback callback = new BaseDialogs.InputDialogCallback() {
            @Override
            public void update(String title) {
                presenter.editTodoList(uuid, title, position);
            }
        };

        showTextInputDialog(fragment, getString(fragment, R.string.edit_topic), callback, oldTitle);
    }

    private static TodoListPresenter instantiatePresenterUsing(Fragment fragment) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
        return new TodoListPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                (TodoListPresenter.View) fragment,
                new RepositoryImpl(repositoryName));
    }
}
