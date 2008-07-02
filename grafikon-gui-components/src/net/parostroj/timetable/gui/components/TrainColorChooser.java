package net.parostroj.timetable.gui.components;

import java.awt.Color;
import net.parostroj.timetable.model.Train;

/**
 * Train color chooser interface - for GTDraw.
 * 
 * @author jub
 */
public interface TrainColorChooser {
    public Color getColor(Train train);
}
