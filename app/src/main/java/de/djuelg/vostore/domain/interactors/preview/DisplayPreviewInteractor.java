package de.djuelg.vostore.domain.interactors.preview;


import java.util.List;

import de.djuelg.vostore.domain.interactors.base.Interactor;
import de.djuelg.vostore.domain.model.preview.Preview;


public interface DisplayPreviewInteractor extends Interactor {

    interface Callback {
        void onPreviewsRetrieved(List<Preview> lists);
    }
}
