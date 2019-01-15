package de.djuelg.vostore.domain.repository;

import de.djuelg.vostore.domain.model.preview.ItemsPerPreview;
import de.djuelg.vostore.domain.model.preview.Preview;

/**
 * A repository that is responsible for the landingpage with previews
 */
public interface PreviewRepository {
    Iterable<Preview> getAll(ItemsPerPreview itemsPerPreview);

    int count();
}
