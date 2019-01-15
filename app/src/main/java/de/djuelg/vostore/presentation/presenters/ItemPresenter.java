package de.djuelg.vostore.presentation.presenters;

import java.util.List;

import de.djuelg.vostore.domain.model.todolist.TodoListHeader;
import de.djuelg.vostore.domain.model.todolist.TodoListItem;
import de.djuelg.vostore.presentation.presenters.base.BasePresenter;


public interface ItemPresenter extends BasePresenter {

    void addItem(String title, boolean important, String Details, String parentTodoListUuid, String parentHeaderUuid);

    void addItemAndAnother(String title, boolean important, String details, String parentTodoListUuid, String parentHeaderUuid);

    void editItem(String uuid, String title, int position, boolean important, String details, boolean done, String parentTodoListUuid, String parentHeaderUuid);

    void addMode(String todoListUuid);

    void editMode(String itemUuid);

    void expandHeaderOfItem(String uuid, String title, int position);

    interface View {

        void itemSynced(boolean addAnother);

        void onHeadersLoaded(List<TodoListHeader> headers);

        void onItemLoaded(TodoListItem item);
    }
}
