package de.djuelg.vostore.domain.interactors.preview;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.preview.TodoList;

/**
 * Created by djuelg on 11.07.17.
 */

public interface DeleteTodoListInteractor extends Interactor {
    interface Callback {
        void onTodoListDeleted(TodoList deletedTodoList);
    }
}
