package de.djuelg.vostore.presentation.presenters;

import java.util.List;

import de.djuelg.vostore.domain.model.preview.Sortation;
import de.djuelg.vostore.presentation.presenters.base.BasePresenter;
import de.djuelg.vostore.presentation.ui.flexibleadapter.PreviewViewModel;

public interface DisplayPreviewPresenter extends BasePresenter {

    void syncPreviews(List<PreviewViewModel> previews);

    void deleteTodoList(String uuid);

    void deleteNote(String uuid);

    List<PreviewViewModel> applySortation(List<PreviewViewModel> previews, Sortation sortation);

    interface View {
        void onPreviewsLoaded(List<PreviewViewModel> previews);

    }
}
