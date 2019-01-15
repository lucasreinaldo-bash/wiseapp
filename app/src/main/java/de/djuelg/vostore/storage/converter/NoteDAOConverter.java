package de.djuelg.vostore.storage.converter;


import com.fernandocejas.arrow.functions.Function;

import org.jetbrains.annotations.Nullable;

import de.djuelg.vostore.domain.model.preview.Note;
import de.djuelg.vostore.storage.model.NoteDAO;

/**
 * Created by djuelg on 04.08.17.
 */

public class NoteDAOConverter implements Function<NoteDAO, Note> {

    @Nullable
    @Override
    public Note apply(NoteDAO input) {
        return RealmConverter.convert(input);
    }
}
