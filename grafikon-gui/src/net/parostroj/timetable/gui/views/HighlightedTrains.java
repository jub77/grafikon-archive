/*
 * HighlightedTrains.java
 * 
 * Created on 23.9.2007, 11:35:42
 */

package net.parostroj.timetable.gui.views;

import java.awt.Color;
import net.parostroj.timetable.gui.ApplicationModelListener;
import net.parostroj.timetable.model.TimeInterval;

/**
 * Highlighted trains.
 * 
 * @author jub
 */
public interface HighlightedTrains extends ApplicationModelListener {
    public boolean isHighlighedInterval(TimeInterval interval);
    
    public Color getColor();
}
