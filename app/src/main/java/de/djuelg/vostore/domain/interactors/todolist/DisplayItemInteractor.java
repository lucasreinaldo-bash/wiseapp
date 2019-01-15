package de.djuelg.vostore.domain.interactors.todolist;


import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.todolist.TodoListItem;


public interface DisplayItemInteractor extends Interactor {

    interface Callback {
        void onItemRetrieved(TodoListItem item);

    }
}
