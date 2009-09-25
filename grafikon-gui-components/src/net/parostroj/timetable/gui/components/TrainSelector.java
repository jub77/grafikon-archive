package net.parostroj.timetable.gui.components;

import net.parostroj.timetable.model.TimeInterval;

/**
 * Selects train after clicking in the graphical timetable view.
 * 
 * @author jub
 */
public interface TrainSelector {
    /**
     * selects trains' interval.
     * 
     * @param interval train's interval to be selected
     */
    public void selectTrainInterval(TimeInterval interval);
    
    /**
     * returns selected train's interval.
     * 
     * @return selected train's interval
     */
    public TimeInterval getSelectedTrainInterval();
}
