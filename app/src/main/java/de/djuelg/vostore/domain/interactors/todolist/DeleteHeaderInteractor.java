package de.djuelg.vostore.domain.interactors.todolist;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.todolist.TodoListHeader;

/**
 * Created by djuelg on 11.07.17.
 */

public interface DeleteHeaderInteractor extends Interactor {
    interface Callback {
        void onHeaderDeleted(TodoListHeader deletedHeader);
    }
}
