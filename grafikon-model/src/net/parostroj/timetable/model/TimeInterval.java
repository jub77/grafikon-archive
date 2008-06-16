package net.parostroj.timetable.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Time interval.
 *
 * @author jub
 */
public class TimeInterval implements AttributesHolder, ObjectWithId {

    private final String id;
    /** Start time. */
    private int start;
    /** End time. */
    private int end;
    /** Train. */
    private Train train;
    /** Owner. */
    private RouteSegment owner;
    /** Track. */
    private Track track;
    /** Speed. */
    private int speed = NO_SPEED;
    /** Attributes. */
    private Attributes attributes;
    /** Time interval type. */
    private TimeIntervalType type;
    /** For tests - overlapping time intervals. */
    private Set<TimeInterval> overlappingIntervals;
    /** No speed constant. */
    public static final int NO_SPEED = -1;
    /** Direction of the time interval regarding the underlying line. */
    private TimeIntervalDirection direction;
    /** Comment. */
    private String comment;

    /**
     * creates instance of an time interval.
     *
     * @param id id
     * @param train train
     * @param owner owner (node track, node, ...)
     * @param start start time
     * @param end end time
     * @param speed speed for line time interval
     * @param direction direction of the line time interval
     * @param type type of the interval
     * @param track track
     */
    public TimeInterval(String id, Train train, RouteSegment owner, int start, int end, int speed, TimeIntervalDirection direction, TimeIntervalType type, Track track) {
        this.train = train;
        this.setOwner(owner);
        this.start = start;
        this.end = end;
        this.type = type;
        this.speed = speed;
        this.direction = direction;
        this.track = track;
        this.attributes = new Attributes();
        this.id = id;
    }

    /**
     * creates instance of time interval.
     * 
     * @param id id
     * @param train train
     * @param owner time interval owner
     * @param start start time
     * @param end end time
     * @param type type of the interval
     * @param track track
     */
    public TimeInterval(String id, Train train, RouteSegment owner, int start, int end, TimeIntervalType type, Track track) {
        this(id, train, owner, start, end, NO_SPEED, null, type, track);
    }

    /**
     * creates copy of an interval.
     * 
     * @param interval copied interval
     */
    public TimeInterval(String id, TimeInterval interval) {
        this(id, interval.getTrain(), interval.getOwner(), interval.getStart(),
                interval.getEnd(), interval.getSpeed(), interval.getDirection(),
                interval.getType(), interval.getTrack());
        this.setComment(interval.getComment());
        this.setAttributes(new Attributes(interval.getAttributes()));
    }

    /**
     * @return end time
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end end time to be set
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @return start time
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start start time to be set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return train
     */
    public Train getTrain() {
        return train;
    }

    /**
     * @param train train to be set
     */
    public void setTrain(Train train) {
        this.train = train;
    }

    /**
     * compares intervals for route part.
     *
     * @param o interval
     * @return comparison
     */
    public int compareToForRoutePart(TimeInterval o) {
        if (o.end < start) {
            return -1;
        }
        if (o.start > end) {
            return 1;
        }
        return 0;
    }

    /**
     * compares intervals for trains.
     *
     * @param o interval
     * @return comparison
     */
    public int compareToForTrain(TimeInterval o) {
        if (o.end <= start) {
            return -1;
        }
        if (o.start >= end) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return train + "(" + start + "," + end + ")";
    }

    /**
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return the type
     */
    public TimeIntervalType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TimeIntervalType type) {
        this.type = type;
    }

    /**
     * @return the track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * @param track the track to set
     */
    public void setTrack(Track track) {
        this.track = track;
    }

    /**
     * @return the overlapping intervals
     */
    public Set<TimeInterval> getOverlappingIntervals() {
        if (overlappingIntervals == null) {
            overlappingIntervals = new HashSet<TimeInterval>();
        }
        return overlappingIntervals;
    }

    /**
     * @return <code>true</code> if there is overlapping interval
     */
    public boolean isOverlapping() {
        return (overlappingIntervals != null) && (overlappingIntervals.size() > 0);
    }

    /**
     * @param overlappingIntervals the collidingIntervals to set
     */
    public void setOverlappingIntervals(Set<TimeInterval> overlappingIntervals) {
        this.overlappingIntervals = overlappingIntervals;
    }

    /**
     * @return <code>false</code> if there is no platform for train that needs one (in any other case it returns <code>true</code>)
     */
    public boolean isPlatformOk() {
        if (type == TimeIntervalType.NODE_END || type == TimeIntervalType.NODE_START || type == TimeIntervalType.NODE_STOP) {
            return !(train.getType().isPlatform() && !((NodeTrack) track).isPlatform());
        } else {
            // otherwise ok
            return true;
        }
    }

    /**
     * shifts time interval with specified amount of time.
     * 
     * @param timeShift shift time
     */
    public void shift(int timeShift) {
        start += timeShift;
        end += timeShift;
    }

    public void move(int aStart) {
        int length = this.getLength();
        this.start = aStart;
        this.end = aStart + length;
    }

    /**
     * returns length of the interval.
     * 
     * @return length of the interval
     */
    public int getLength() {
        return end - start;
    }

    /**
     * sets length of the interval.
     * 
     * @param length new length of the interval
     */
    public void setLength(int length) {
        end = start + length;
    }

    /**
     * @return owner of this time interval
     */
    public RouteSegment getOwner() {
        return owner;
    }

    /**
     * @param owner new owner to be set
     */
    public void setOwner(RouteSegment owner) {
        this.owner = owner;
    }

    /**
     * @return direction
     */
    public TimeIntervalDirection getDirection() {
        return direction;
    }

    /**
     * @param direction new direction to be set
     */
    public void setDirection(TimeIntervalDirection direction) {
        this.direction = direction;
    }

    /**
     * @return from node for interval that belongs to line otherwise <code>null</code>
     */
    public Node getFrom() {
        return (owner instanceof Line) ? ((Line) owner).getFrom(direction) : null;
    }

    /**
     * @return to node for interval that belongs to line otherwise <code>null</code>
     */
    public Node getTo() {
        return (owner instanceof Line) ? ((Line) owner).getTo(direction) : null;
    }

    /**
     * @return from node track straight for interval that belongs to line otherwise <code>null</code>
     */
    public NodeTrack getFromStraightTrack() {
        return (owner instanceof Line) ? ((LineTrack) track).getFromStraightTrack(direction) : null;
    }

    /**
     * @return to node straight track for interval that belongs to line otherwise <code>null</code>
     */
    public NodeTrack getToStraightTrack() {
        return (owner instanceof Line) ? ((LineTrack) track).getToStraightTrack(direction) : null;
    }

    /**
     * @return comment to this time interval
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void removeFromOwner() {
        owner.removeTimeInterval(this);
    }

    public void addToOwner() {
        owner.addTimeInterval(this);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public Object removeAttribute(String key) {
        return attributes.remove(key);
    }
    
    public boolean isNodeOwner() {
        return (owner instanceof Node);
    }
    
    public boolean isLineOwner() {
        return (owner instanceof Line);
    }
    
    public Node getOwnerAsNode() {
        return isNodeOwner() ? (Node)owner : null;
    }
    
    public Line getOwnerAsLine() {
        return isLineOwner() ? (Line)owner : null;
    }

    @Override
    public String getId() {
        return id;
    }
}
