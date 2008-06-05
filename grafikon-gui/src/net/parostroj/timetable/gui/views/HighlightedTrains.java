/*
 * HighlightedTrains.java
 * 
 * Created on 23.9.2007, 11:35:42
 */

package net.parostroj.timetable.gui.views;

import java.awt.Color;
import java.util.Set;
import net.parostroj.timetable.gui.ApplicationModelListener;
import net.parostroj.timetable.model.Train;

/**
 * Highlighted trains.
 * 
 * @author jub
 */
public interface HighlightedTrains extends ApplicationModelListener {
    public Set<Train> getHighlighedTrains();
    
    public Color getColor();
}
