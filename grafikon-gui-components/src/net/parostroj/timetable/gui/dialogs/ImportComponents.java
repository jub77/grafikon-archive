package net.parostroj.timetable.gui.dialogs;

import net.parostroj.timetable.gui.utils.ResourceLoader;

/**
 * Export/Import components.
 *
 * @author jub
 */
public enum ImportComponents {
    STATIONS("import.stations"),
    TRAIN_TYPES("import.train_types");

    private String key;

    private ImportComponents(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return ResourceLoader.getString(key);
    }
}
