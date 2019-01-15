package de.djuelg.vostore.domain.interactors.todolist;


import java.util.List;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.todolist.TodoListHeader;


public interface DisplayHeadersInteractor extends Interactor {

    interface Callback {
        void onHeadersRetrieved(List<TodoListHeader> headers);

    }
}
