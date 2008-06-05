/*
 * TrainColorChooser.java
 * 
 * Created on 16.9.2007, 19:03:21
 */
package net.parostroj.timetable.gui.views;

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
