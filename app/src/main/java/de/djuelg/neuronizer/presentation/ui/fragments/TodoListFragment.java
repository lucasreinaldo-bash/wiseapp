package de.djuelg.neuronizer.presentation.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fernandocejas.arrow.collections.Iterables;
import com.fernandocejas.arrow.optional.Optional;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.presentation.presenters.DisplayTodoListPresenter;
import de.djuelg.neuronizer.presentation.presenters.HeaderPresenter;
import de.djuelg.neuronizer.presentation.presenters.impl.DisplayTodoListPresenterImpl;
import de.djuelg.neuronizer.presentation.ui.custom.FragmentInteractionListener;
import de.djuelg.neuronizer.presentation.ui.custom.ShareIntent;
import de.djuelg.neuronizer.presentation.ui.custom.view.FlexibleRecyclerView;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListHeaderViewModel;
import de.djuelg.neuronizer.presentation.ui.flexibleadapter.TodoListItemViewModel;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.Payload;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.helpers.ActionModeHelper;
import eu.davidea.flexibleadapter.helpers.UndoHelper;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;

import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_ACTIVE_REPO;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREF_HEADER_OR_ITEM;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TITLE;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_UUID;
import static de.djuelg.neuronizer.presentation.ui.Constants.SWIPE_LEFT_TO_EDIT;
import static de.djuelg.neuronizer.presentation.ui.Constants.SWIPE_RIGHT_TO_DELETE;
import static de.djuelg.neuronizer.presentation.ui.custom.FlexibleAdapterConfiguration.setupFlexibleAdapter;
import static de.djuelg.neuronizer.presentation.ui.custom.view.Animations.fadeIn;
import static de.djuelg.neuronizer.presentation.ui.custom.view.Animations.fadeOut;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarColor;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.changeAppbarTitle;
import static de.djuelg.neuronizer.presentation.ui.custom.view.AppbarCustomizer.fontifyString;
import static de.djuelg.neuronizer.presentation.ui.dialog.HeaderDialogs.showAddHeaderDialog;
import static de.djuelg.neuronizer.presentation.ui.dialog.HeaderDialogs.showEditHeaderDialog;
import static de.djuelg.neuronizer.storage.RepositoryManager.FALLBACK_REALM;

/**
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends Fragment implements View.OnClickListener, DisplayTodoListPresenter.View, HeaderPresenter.View,
        FlexibleAdapter.OnItemSwipeListener, FlexibleAdapter.OnItemLongClickListener, ActionMode.Callback, UndoHelper.OnUndoListener {

    @Bind(R.id.fab_add_header) FloatingActionButton mFabHeader;
    @Bind(R.id.fab_menu) FloatingActionMenu mFabMenu;
    @Bind(R.id.fab_menu_header) FloatingActionButton mFabHeaderMenu;
    @Bind(R.id.fab_menu_item) FloatingActionButton mFabItemMenu;
    @Bind(R.id.todo_list_recycler_view) FlexibleRecyclerView mRecyclerView;
    @Bind(R.id.todo_list_empty_recycler_view) RelativeLayout mEmptyView;

    private DisplayTodoListPresenter mPresenter;
    private FragmentInteractionListener mListener;
    private FlexibleAdapter<AbstractFlexibleItem> mAdapter;
    private ActionModeHelper mActionModeHelper;
    private SharedPreferences sharedPreferences;
    private String uuid;
    private String title;

    public TodoListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static TodoListFragment newInstance(String uuid, String title) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UUID, uuid);
        args.putString(KEY_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            uuid = bundle.getString(KEY_UUID);
            title = bundle.getString(KEY_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        ButterKnife.bind(this, view);
        mFabHeader.setHideAnimation(fadeOut());
        mFabHeader.setShowAnimation(fadeIn());
        mFabMenu.setMenuButtonHideAnimation(fadeOut());
        mFabMenu.setMenuButtonShowAnimation(fadeIn());
        mFabHeader.setOnClickListener(this);
        mFabHeaderMenu.setOnClickListener(this);
        mFabItemMenu.setOnClickListener(this);
        changeAppbarTitle(getActivity(), title);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String repositoryName = sharedPreferences.getString(KEY_PREF_ACTIVE_REPO, FALLBACK_REALM);
        // create a presenter for this view
        mPresenter = new DisplayTodoListPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new TodoListRepositoryImpl(repositoryName)
        );

        // let's load list when the app resumes
        mPresenter.loadTodoList(uuid);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mActionModeHelper != null) mActionModeHelper.destroyActionModeIfCan();
        onDeleteConfirmed(0);
        mPresenter.syncTodoList(mAdapter.getHeaderItems());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_todo_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                mListener.onSettingsSelected();
                return true;
            case R.id.action_share:
                ShareIntent.withTitle(title).withItems(mAdapter.getCurrentItems()).send(getContext());
                return true;
        }
        return false;
    }

    @Override
    public void onTodoListLoaded(List<AbstractFlexibleItem> items) {
        boolean permanentDelete = !sharedPreferences.getBoolean(KEY_PREF_HEADER_OR_ITEM, true);

        mAdapter = new FlexibleAdapter<>(items);
        mRecyclerView.configure(mEmptyView, mAdapter, mFabMenu);
        setupFlexibleAdapter(this, mAdapter, permanentDelete);
        initializeActionModeHelper();
    }

    @Override
    public void onTodoListReloaded(List<AbstractFlexibleItem> items) {
        mAdapter.clearSelection();
        mAdapter.updateDataSet(items, true);
    }

    @Override
    public void onClick(View view) {
        // Currently there is only FAB
        switch (view.getId()) {
            case R.id.fab_add_header:
            case R.id.fab_menu_header:
                showAddHeaderDialog(this, uuid);
                mFabMenu.close(true);
                break;
            case R.id.fab_menu_item:
                mListener.onAddItem(uuid);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHeaderAdded() {
        mPresenter.loadTodoList(uuid);
    }

    @Override
    public void onHeaderEdited() {
        mPresenter.loadTodoList(uuid);
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        switch (direction) {
            case SWIPE_RIGHT_TO_DELETE:
                deleteItem(position);
                break;
            case SWIPE_LEFT_TO_EDIT:
                editItem(position);
                break;
        }
    }

    private void deleteItem(int position) {
        mAdapter.clearSelection();
        mAdapter.addSelection(position);
        deleteSelectedItemOrHeader();
    }

    private void editItem(int position) {
        Optional<TodoListItemViewModel> vm = Optional.fromNullable((TodoListItemViewModel) mAdapter.getItem(position));
        if (vm.isPresent()) mListener.onEditItem(uuid, vm.get().getItem().getUuid());
    }

    @Override
    public void onActionStateChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // Nothing to do
    }

    private void initializeActionModeHelper() {
        //this = ActionMode.Callback instance
        mActionModeHelper = new ActionModeHelper(mAdapter, R.menu.menu_action_header, this) {
            // Override to customize the title
            @Override
            public void updateContextTitle(int count) {
                // You can use the internal mActionMode instance
                if (mActionMode != null) {
                    int position = mAdapter.getSelectedPositions().get(0);
                    AbstractFlexibleItem item = mAdapter.getItem(position);
                    mActionMode.setTitle(fontifyString(getActivity(), getString(R.string.action_edit_category, item)));
                }
            }
        }.withDefaultMode(SelectableAdapter.Mode.SINGLE);
        mActionModeHelper.withDefaultMode(SelectableAdapter.Mode.SINGLE);
        mAdapter.setMode(SelectableAdapter.Mode.SINGLE);
    }

    @Override
    public void onItemLongClick(int position) {
        AbstractFlexibleItem item = mAdapter.getItem(position);
        if (item instanceof TodoListHeaderViewModel) {
            mAdapter.clearSelection();
            mActionModeHelper.onLongClick((AppCompatActivity) getActivity(), position);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // No prepare needed
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteSelectedItemOrHeader();
                return true;
            case R.id.action_edit:
                editSelectedHeader();
                return true;
            default:
                return false;
        }
    }

    private void deleteSelectedItemOrHeader() {
        int position = mAdapter.getSelectedPositions().get(0);

        if (mAdapter.isPermanentDelete()) {
            permanentDeleteItem(position);
            return;
        }

        String message = getString(R.string.deleted_snackbar, mAdapter.getItem(position));
        UndoHelper.OnActionListener removeListener = new UndoHelper.SimpleActionListener() {
            @Override
            public void onPostAction() {
                // Finish the action mode
                mActionModeHelper.destroyActionModeIfCan();
                mRecyclerView.onAdapterMaybeEmpty();
            }
        };
        new UndoHelper(mAdapter, this).withPayload(Payload.CHANGE)
                .withAction(UndoHelper.ACTION_REMOVE, removeListener).remove(
                mAdapter.getSelectedPositions(), getView(), message, getString(R.string.undo), UndoHelper.UNDO_TIMEOUT);
    }

    private void editSelectedHeader() {
        int position =  mAdapter.getSelectedPositions().get(0);
        TodoListHeaderViewModel headerVH = (TodoListHeaderViewModel) mAdapter.getItem(position);
        if (headerVH != null) {
            TodoListHeader header = headerVH.getHeader();
            showEditHeaderDialog(this, header.getUuid(), header.getTitle(), header.getPosition(), header.isExpanded());
        }
        mActionModeHelper.destroyActionModeIfCan();
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        changeAppbarColor(getActivity(), R.color.colorPrimary);
        for (IHeader header : mAdapter.getHeaderItems()) {
            TodoListHeaderViewModel vm = (TodoListHeaderViewModel) header;
            vm.setExpanded(vm.getHeader().isExpanded());
        }
    }

    @Override
    public void onUndoConfirmed(int action) {
        if (!isVisible() || mAdapter == null || action != UndoHelper.ACTION_REMOVE) return;
        List<AbstractFlexibleItem> restoredItems = mAdapter.getDeletedItems();
        mAdapter.restoreDeletedItems();
        if (!restoredItems.isEmpty()) {
            mAdapter.clearSelection();
            AbstractFlexibleItem item = Iterables.getLast(restoredItems);
            if (item instanceof TodoListHeaderViewModel && mActionModeHelper != null) mActionModeHelper
                    .onLongClick((AppCompatActivity) getActivity(), mAdapter.getGlobalPositionOf(item));
        }
    }

    @Override
    public void onDeleteConfirmed(int action) {
        if (mAdapter == null) return;
        for (AbstractFlexibleItem adapterItem : mAdapter.getDeletedItems()) {
            switch (adapterItem.getLayoutRes()) {
                case R.layout.todo_list_header:
                    if (mAdapter.isPermanentDelete() && mActionModeHelper != null) mActionModeHelper.destroyActionModeIfCan();
                    mPresenter.deleteHeader(((TodoListHeaderViewModel)adapterItem).getHeader().getUuid());
                    break;
                case R.layout.todo_list_item:
                    mPresenter.deleteItem(((TodoListItemViewModel)adapterItem).getItem().getUuid());
                    break;
            }
        }
        mListener.onUpdateAllWidgets();
    }

    public void permanentDeleteItem(int position) {
        AbstractFlexibleItem adapterItem = mAdapter.getItem(position);
        if (adapterItem == null) return;
        switch (adapterItem.getLayoutRes()) {
            case R.layout.todo_list_header:
                if (mActionModeHelper != null) mActionModeHelper.destroyActionModeIfCan();
                mPresenter.deleteHeader(((TodoListHeaderViewModel)adapterItem).getHeader().getUuid());
                break;
            case R.layout.todo_list_item:
                mPresenter.deleteItem(((TodoListItemViewModel)adapterItem).getItem().getUuid());
                break;
        }
        mAdapter.removeItem(position);
    }
}
