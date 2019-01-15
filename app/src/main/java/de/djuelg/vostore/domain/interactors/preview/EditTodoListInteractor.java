package de.djuelg.vostore.domain.interactors.preview;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.preview.TodoList;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditTodoListInteractor extends Interactor {
    interface Callback {
        void onTodoListUpdated(TodoList updatedTodoList);
    }
}