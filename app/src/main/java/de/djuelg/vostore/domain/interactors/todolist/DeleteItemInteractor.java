package de.djuelg.vostore.domain.interactors.todolist;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.todolist.TodoListItem;

/**
 * Created by djuelg on 11.07.17.
 */

public interface DeleteItemInteractor extends Interactor {
    interface Callback {
        void onItemDeleted(TodoListItem deletedItem);
    }
}
