package de.djuelg.vostore.presentation.presenters;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.model.preview.Note;
import de.djuelg.vostore.presentation.presenters.base.BasePresenter;


public interface DisplayNotePresenter extends BasePresenter {

    void loadNote(String uuid);

    void editNote(String uuid, String body);

    interface View {

        void onNoteLoaded(Optional<Note> note);
    }
}
