package net.parostroj.timetable.model;

/**
 * Line track.
 * 
 * @author jub
 */
public class LineTrack extends Track {

    private NodeTrack fromStraightTrack;
    private NodeTrack toStraightTrack;

    /**
     * Constructor.
     * 
     * @param id id
     */
    public LineTrack(String id) {
        super(id);
    }

    /**
     * Constructor with number of the track.
     * 
     * @param id id
     * @param number track number
     */
    public LineTrack(String id, String number) {
        super(id, number);
    }

    public NodeTrack getFromStraightTrack() {
        return fromStraightTrack;
    }

    public void setFromStraightTrack(NodeTrack fromStraightTrack) {
        this.fromStraightTrack = fromStraightTrack;
    }

    public NodeTrack getToStraightTrack() {
        return toStraightTrack;
    }

    public void setToStraightTrack(NodeTrack toStraightTrack) {
        this.toStraightTrack = toStraightTrack;
    }

    public NodeTrack getFromStraightTrack(TimeIntervalDirection direction) {
        return (direction == TimeIntervalDirection.FORWARD) ? fromStraightTrack : toStraightTrack;
    }

    public NodeTrack getToStraightTrack(TimeIntervalDirection direction) {
        return (direction == TimeIntervalDirection.FORWARD) ? toStraightTrack : fromStraightTrack;
    }
}
