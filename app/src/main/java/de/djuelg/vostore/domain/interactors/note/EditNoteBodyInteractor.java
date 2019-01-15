package de.djuelg.vostore.domain.interactors.note;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.preview.Note;

/**
 * Created by djuelg on 10.07.17.
 */

public interface EditNoteBodyInteractor extends Interactor {
    interface Callback {
        void onNoteUpdated(Note updatedNote);
    }
}