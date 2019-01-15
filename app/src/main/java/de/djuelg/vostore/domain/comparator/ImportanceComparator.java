package de.djuelg.vostore.domain.comparator;

import java.util.Comparator;

/**
 * Created by Domi on 06.08.2017.
 */

public class ImportanceComparator implements Comparator<PreviewCompareable> {

    @Override
    public int compare(PreviewCompareable first, PreviewCompareable scnd) {
        return Long.compare(scnd.getImportance(), first.getImportance());
    }
}
