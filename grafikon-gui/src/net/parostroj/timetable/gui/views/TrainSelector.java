package net.parostroj.timetable.gui.views;

import net.parostroj.timetable.model.Train;

/**
 * Selects train after clicking in the graphical timetable view.
 * 
 * @author jub
 */
public interface TrainSelector {
    /**
     * selects train.
     * 
     * @param train train to be selected
     */
    public void selectTrain(Train train);
    
    /**
     * returns selected train.
     * 
     * @return selected train
     */
    public Train getSelectedTrain();
}
