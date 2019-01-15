package de.djuelg.vostore.presentation.presenters;

import de.djuelg.vostore.presentation.presenters.base.BasePresenter;


public interface HeaderPresenter extends BasePresenter {

    void addHeader(String title, String parentTodoListUuid);

    void editHeader(String uuid, String title, int position, boolean expanded);

    interface View {
        void onHeaderAdded();

        void onHeaderEdited();
    }
}
