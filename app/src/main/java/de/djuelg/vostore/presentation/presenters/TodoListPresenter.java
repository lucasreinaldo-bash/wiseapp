package de.djuelg.vostore.presentation.presenters;

import de.djuelg.vostore.presentation.presenters.base.BasePresenter;


public interface TodoListPresenter extends BasePresenter {

    void addTodoList(String title);

    void editTodoList(String uuid, String title, int position);

    interface View {

        void onTodoListAdded(String uuid, String title);

        void onTodoListEdited(String uuid, String title);
    }
}
