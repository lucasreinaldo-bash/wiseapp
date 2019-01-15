package de.djuelg.vostore.domain.interactors.todolist;

import de.djuelg.vostore.domain.interactors.base.Interactor;

/**
 * Created by djuelg on 09.07.17.
 *
 */

public interface AddItemInteractor extends Interactor {
    interface Callback {
        void onItemAdded();

        void onParentNotFound();
    }
}
