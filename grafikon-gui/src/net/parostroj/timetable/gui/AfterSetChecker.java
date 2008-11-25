package net.parostroj.timetable.gui;

import net.parostroj.timetable.model.TrainDiagram;

/**
 * Checks loaded diagram. Adds missing values and other information.
 *
 * @author jub
 */
public class AfterSetChecker {
    private static final Integer STATION_TRANSFER_TIME = 10;

    public void check(TrainDiagram diagram) {
        // empty diagram doesn't have to be checked :)
        if (diagram == null)
            return;

        // add transfer time if missing
        if (diagram.getAttribute("station.transfer.time") == null) {
            diagram.setAttribute("station.transfer.time", STATION_TRANSFER_TIME);
        }
    }
}
