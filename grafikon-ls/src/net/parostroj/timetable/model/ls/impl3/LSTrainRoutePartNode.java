package net.parostroj.timetable.model.ls.impl3;

import net.parostroj.timetable.model.TimeInterval;

/**
 * Route part for node.
 * 
 * @author jub
 */
public class LSTrainRoutePartNode {

    private String nodeId;
    private String trackId;
    private int stop;

    public LSTrainRoutePartNode() {
    }

    public LSTrainRoutePartNode(TimeInterval interval) {
        nodeId = interval.getOwner().getId();
        trackId = interval.getTrack().getId();
        stop = interval.getLength();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
}
