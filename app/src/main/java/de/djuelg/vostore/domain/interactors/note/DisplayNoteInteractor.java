package de.djuelg.vostore.domain.interactors.note;


import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.preview.Note;


public interface DisplayNoteInteractor extends Interactor {

    interface Callback {
        void onNoteRetrieved(Optional<Note> note);
    }
}
