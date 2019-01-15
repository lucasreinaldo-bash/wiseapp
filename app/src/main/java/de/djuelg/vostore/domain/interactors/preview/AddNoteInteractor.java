package de.djuelg.vostore.domain.interactors.preview;

import de.djuelg.vostore.domain.interactors.base.Interactor;

/**
 * Created by djuelg on 09.07.17.
 *
 */

public interface AddNoteInteractor extends Interactor {
    interface Callback {
        void onNoteAdded(String uuid, String title);
    }
}
