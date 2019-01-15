package de.djuelg.vostore.domain.model.preview;

import de.djuelg.vostore.domain.comparator.PositionCompareable;
import de.djuelg.vostore.domain.model.BaseModel;

/**
 * Created by djuelg on 15.10.17.
 */

public interface Preview extends PositionCompareable {

    BaseModel getBaseItem();

    String getSubtitle();

    String getDetails();

    long calculateImportance();

    @Override
    int getPosition();
}
