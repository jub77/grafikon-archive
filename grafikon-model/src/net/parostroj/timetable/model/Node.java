package net.parostroj.timetable.model;

import java.util.*;

/**
 * Station consists of several tracks. Each tracks provides its own list
 * of time intervals.
 *
 * @author jub
 */
public class Node implements RouteSegment, AttributesHolder, ObjectWithId {

    /** ID. */
    private final String id;
    /** Name of the node. */
    private String name;
    /** Abbreviation. */
    private String abbr;
    /** Attributes of the node. */
    private Attributes attributes;
    /** List of node tracks. */
    private List<NodeTrack> tracks;
    /** Node type. */
    private NodeType type;
    private int positionX;
    private int positionY;

    /**
     * Initialization.
     */
    private void init() {
        tracks = new LinkedList<NodeTrack>();
        attributes = new Attributes();
    }

    /**
     * Default constructor.
     * 
     * @param id id
     * @param type type
     */
    public Node(String id, NodeType type) {
        init();
        this.type = type;
        this.id = id;
    }

    /**
     * creates instance with specified name.
     *
     * @param id id
     * @param type type
     * @param name name
     * @param abbr abbreviation
     */
    public Node(String id, NodeType type, String name, String abbr) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.abbr = abbr;
        init();
    }

    /**
     * @return id of the node
     */
    @Override
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see net.parostroj.timetable.model.RoutePoint#getTimeInterval(net.parostroj.timetable.model.Train)
     */
    @Override
    public TimeInterval getTimeInterval(Train train) {
        for (NodeTrack stationTrack : tracks) {
            TimeInterval interval = stationTrack.getTimeInterval(train);
            if (interval != null) {
                return interval;
            }
        }
        return null;
    }

    public TimeInterval createTimeInterval(String intervalId, Train train, int start, TrainDiagram diagram, TimeIntervalType type, int defaultStop) {
        int end = start + this.computeStopTime(train, diagram, type, defaultStop);

        boolean stop = type == TimeIntervalType.NODE_END || type == TimeIntervalType.NODE_START || type == TimeIntervalType.NODE_STOP;

        NodeTrack selectedTrack = null;
        TimeInterval interval = new TimeInterval(null, train, this, start, end, type, null);

        // check which platform is free for adding
        for (NodeTrack nodeTrack : tracks) {
            // skip station tracks with no platform
            if (stop && train.getType().isPlatform() && !nodeTrack.isPlatform()) {
                continue;
            }
            TimeIntervalResult result = nodeTrack.testTimeInterval(interval);
            if (result.getStatus() == TimeIntervalResult.Status.OK) {
                selectedTrack = nodeTrack;
                break;
            }
        }

        if (selectedTrack == null) {
            // set first one
            selectedTrack = tracks.get(0);
        }

        return new TimeInterval(intervalId, train, this, start, end, type, selectedTrack);
    }

    /**
     * @return the node tracks
     */
    @Override
    public List<NodeTrack> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void addTrack(NodeTrack track) {
        tracks.add(track);
    }

    public void addTrack(NodeTrack track, int position) {
        tracks.add(position, track);
    }

    public void removeTrack(NodeTrack track) {
        tracks.remove(track);
    }

    public void removeAllTracks() {
        tracks.clear();
    }

    /**
     * computes stop time for specified train.
     *
     * @param train train
     * @param data model info
     * @param type time interval type
     * @param defaultStop default stop time
     * @return stop time
     */
    private int computeStopTime(Train train, TrainDiagram diagram, TimeIntervalType type, int defaultStop) {
        if (type == TimeIntervalType.NODE_STOP) {
            return defaultStop;
        } else {
            return 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public Object removeAttribute(String key) {
        return this.attributes.remove(key);
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
    public String toString() {
        return getName();
    }

    @Override
    public void removeTimeInterval(TimeInterval interval) {
        for (NodeTrack track : tracks) {
            track.removeTimeInterval(interval);
        }
    }

    @Override
    public void addTimeInterval(TimeInterval interval) {
        interval.getTrack().addTimeInterval(interval);
    }

    @Override
    public Line asLine() {
        return null;
    }

    @Override
    public Node asNode() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        for (NodeTrack track : tracks) {
            if (!track.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns node track with specified number.
     * 
     * @param number number
     * @return node track
     */
    public NodeTrack getNodeTrackByNumber(String number) {
        for (NodeTrack track : tracks) {
            if (track.getNumber().equals(number)) {
                return track;
            }
        }
        return null;
    }

    /**
     * returns set of overlapping intervals for all node tracks for given
     * time interval.
     * 
     * @param interval checked interval
     * @return set of overlapping time intervals
     */
    public Set<TimeInterval> getOverlappingTimeIntervals(TimeInterval interval) {
        Set<TimeInterval> out = null;
        for (NodeTrack track : tracks) {
            TimeIntervalResult result = track.testTimeIntervalOI(interval);
            if (result.getStatus() == TimeIntervalResult.Status.OVERLAPPING) {
                if (out == null) {
                    out = new HashSet<TimeInterval>(result.getOverlappingIntervals());
                } else {
                    out.addAll(result.getOverlappingIntervals());
                }
            }
        }
        if (out == null) {
            return Collections.emptySet();
        } else {
            return out;
        }
    }

    @Override
    public NodeTrack findTrackById(String id) {
        for (NodeTrack track : getTracks()) {
            if (track.getId().equals(id)) {
                return track;
            }
        }
        return null;
    }
}
