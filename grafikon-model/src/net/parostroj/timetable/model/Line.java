package net.parostroj.timetable.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.parostroj.timetable.utils.Pair;

/**
 * Track between two route points.
 *
 * @author jub
 */
public class Line implements RouteSegment, AttributesHolder {

    /** ID. */
    private final String id;
    /** Length in mm. */
    private int length;
    /** List of node tracks. */
    private List<LineTrack> tracks;
    /** Top speed for the track. */
    private int topSpeed = UNLIMITED_SPEED;
    /** Unlimited spedd. */
    public static final int UNLIMITED_SPEED = -1;
    /** No speed constant. */
    public static final int NO_SPEED = UNLIMITED_SPEED;
    /** Attributes. */
    private Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
    /** Starting point. */
    private Node from;
    /** Ending point. */
    private Node to;

    /**
     * creates track with specified length.
     *
     * @param id id
     * @param length length of the track in milimeters
     * @param from starting point
     * @param to end point
     */
    public Line(String id, int length, Node from, Node to) {
        tracks = new ArrayList<LineTrack>();
        attributes = new Attributes();
        this.length = length;
        this.from = from;
        this.to = to;
        this.id = id;
    }

    /**
     * creates track.
     * 
     * @param id id
     * @param length length of the track in milimeters
     * @param from starting point
     * @param to end point
     * @param topSpeed top speed
     */
    public Line(String id, int length, Node from, Node to, int topSpeed) {
        this(id, length, from, to);
        this.topSpeed = topSpeed;
    }

    /**
     * @return id of the line
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @return track length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length length to be set
     */
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public TimeInterval getTimeInterval(Train train) {
        for (LineTrack track : tracks) {
            TimeInterval interval = track.getTimeInterval(train);
            if (interval != null) {
                return interval;
            }
        }
        return null;
    }

    public TimeInterval createTimeInterval(String intervalId, Train train, int start, TrainDiagram diagram, TimeIntervalType type, TimeIntervalDirection direction, int prefferedSpeed) {
        Pair<Integer, Integer> computed = this.computeRunningTime(train, prefferedSpeed, diagram, type);
        int end = start + computed.first;

        LineTrack selectedTrack = null;
        TimeInterval interval = new TimeInterval(null, train, this, start, end, computed.second, direction, type, null);

        // check which track is free for adding
        for (LineTrack lineTrack : tracks) {
            TimeIntervalResult result = lineTrack.testTimeInterval(interval);
            if (result.getStatus() == TimeIntervalResult.Status.OK) {
                selectedTrack = lineTrack;
                break;
            }
        }

        if (selectedTrack == null) {
            // set first one
            selectedTrack = tracks.get(0);
        }

        return new TimeInterval(intervalId, train, this, start, end, computed.second, direction, type, selectedTrack);
    }

    /**
     * @return tracks
     */
    @Override
    public List<LineTrack> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void addTrack(LineTrack track) {
        tracks.add(track);
    }

    public void addTrack(LineTrack track, int position) {
        tracks.add(position, track);
    }

    public void removeTrack(LineTrack track) {
        tracks.remove(track);
    }

    public void removeAllTracks() {
        tracks.clear();
    }

    /**
     * @return top speed
     */
    public int getTopSpeed() {
        return topSpeed;
    }

    /**
     * @param topSpeed top speed to be set
     */
    public void setTopSpeed(int topSpeed) {
        if (topSpeed == 0 || topSpeed < -1) {
            throw new IllegalArgumentException("Top speed should be positive number.");
        }
        this.topSpeed = topSpeed;
    }

    /**
     * computes running time for this track.
     *
     * @param train train
     * @param prefferedSpeed preffered speed
     * @param data data
     * @param type type of the interval
     * @return pair running time and speed
     */
    public Pair<Integer, Integer> computeRunningTime(Train train, int prefferedSpeed, TrainDiagram diagram, TimeIntervalType type) {
        Scale scale = (Scale) diagram.getAttribute("scale");
        double timeScale = (Double) diagram.getAttribute("time.scale");

        int speed;
        if (prefferedSpeed != NO_SPEED) {
            speed = Math.min(prefferedSpeed, train.getTopSpeed());
        } else {
            // apply max train speed
            speed = train.getTopSpeed();
        }

        // apply track speed limit
        if (this.topSpeed != UNLIMITED_SPEED) {
            speed = Math.min(speed, this.topSpeed);
        }
        // compute time for the train
        int time = (int) Math.floor((((double) length) * scale.getRatio() * timeScale * 3.6) / (speed * 1000));

        // apply speeding and braking penalties
        PenaltyTable penaltyTable = RunningTimeComputationHelper.getPenaltyTable();
        if (type == TimeIntervalType.LINE_THROUGH_STOP || type == TimeIntervalType.LINE_START_STOP) {
            time = time + penaltyTable.getBrakingTimePenalty(train.getType().getSbType(), speed, timeScale);
        }
        if (type == TimeIntervalType.LINE_START_THROUGH || type == TimeIntervalType.LINE_START_STOP) {
            time = time + penaltyTable.getSpeedingTimePenalty(train.getType().getSbType(), speed, timeScale);
        }

        // rounding to minutes (1 - 19 .. down, 20 - 59 .. up)
        time = ((time + 40) / 60) * 60;

        return new Pair<Integer, Integer>(time, speed);
    }

    @Override
    public void removeTimeInterval(TimeInterval interval) {
        interval.getTrack().removeTimeInterval(interval);
    }

    @Override
    public String toString() {
        return from.getAbbr() + "-" + to.getAbbr() + " <" + length + "," + topSpeed + ">";
    }

    @Override
    public void addTimeInterval(TimeInterval interval) {
        interval.getTrack().addTimeInterval(interval);
    }

    @Override
    public Line asLine() {
        return this;
    }

    @Override
    public Node asNode() {
        return null;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public Node getFrom(TimeIntervalDirection direction) {
        return (direction == TimeIntervalDirection.FORWARD) ? from : to;
    }

    public Node getTo(TimeIntervalDirection direction) {
        return (direction == TimeIntervalDirection.FORWARD) ? to : from;
    }

    @Override
    public boolean isEmpty() {
        for (LineTrack track : tracks) {
            if (!track.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns line track with specified number.
     * 
     * @param number number
     * @return line track
     */
    public LineTrack getLineTrackByNumber(String number) {
        for (LineTrack track : tracks) {
            if (track.getNumber().equals(number)) {
                return track;
            }
        }
        return null;
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

    @Override
    public LineTrack findTrackById(String id) {
        for (LineTrack track : getTracks()) {
            if (track.getId().equals(id)) {
                return track;
            }
        }
        return null;
    }
}
