package net.parostroj.timetable.model.ls.impl3;

import net.parostroj.timetable.model.TimeInterval;

/**
 * Route part for line.
 * 
 * @author jub
 */
public class LSTrainRoutePartLine {

    private String lineId;
    private String trackId;
    private int speed;

    public LSTrainRoutePartLine() {
    }

    public LSTrainRoutePartLine(TimeInterval interval) {
        lineId = interval.getOwner().getId();
        trackId = interval.getTrack().getId();
        speed = interval.getSpeed();
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
}
