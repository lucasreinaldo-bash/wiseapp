package de.djuelg.vostore.domain.interactors.todolist;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.todolist.TodoListItem;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditItemInteractor extends Interactor {
    interface Callback {
        void onItemUpdated(TodoListItem updatedItem);

    }
}