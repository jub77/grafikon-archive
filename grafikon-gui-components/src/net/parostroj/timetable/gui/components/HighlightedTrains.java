package net.parostroj.timetable.gui.components;

import java.awt.Color;
import java.util.Set;
import net.parostroj.timetable.model.Train;

/**
 * Highlighted trains.
 * 
 * @author jub
 */
public interface HighlightedTrains {
    public Set<Train> getHighlighedTrains();
    
    public Color getColor();
}
