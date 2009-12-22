package net.parostroj.timetable.gui.dialogs;

import java.util.ArrayList;
import net.parostroj.timetable.gui.AppPreferences;
import net.parostroj.timetable.gui.StorableGuiData;

/**
 * List of floating dialogs.
 *
 * @author jub
 */
public class FloatingDialogsList extends ArrayList<FloatingDialog> implements StorableGuiData {

    @Override
    public void saveToPreferences(AppPreferences prefs) {
        for (FloatingDialog dialog : this)
            dialog.saveToPreferences(prefs);
    }

    @Override
    public void loadFromPreferences(AppPreferences prefs) {
        for (FloatingDialog dialog : this)
            dialog.loadFromPreferences(prefs);
    }
    
}
