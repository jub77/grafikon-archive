package net.parostroj.timetable.model;

import java.util.*;
import net.parostroj.timetable.model.events.NodeEvent;
import net.parostroj.timetable.model.events.NodeListener;

/**
 * Node that can consist of several tracks. Each tracks provides its own list
 * of time intervals.
 *
 * @author jub
 */
public class Node implements RouteSegment, AttributesHolder, ObjectWithId {

    /** Train diagram. */
    private final TrainDiagram diagram;
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
    /** X position in gui. */
    private int positionX;
    /** Y position in gui. */
    private int positionY;
    private GTListenerSupport<NodeListener, NodeEvent> listenerSupport;

    /**
     * Initialization.
     */
    private void init() {
        tracks = new LinkedList<NodeTrack>();
        attributes = new Attributes();
        listenerSupport = new GTListenerSupport<NodeListener, NodeEvent>(new GTEventSender<NodeListener, NodeEvent>() {

            @Override
            public void fireEvent(NodeListener listener, NodeEvent event) {
                listener.nodeChanged(event);
            }
        });
    }

    /**
     * creates instance with specified name.
     *
     * @param id id
     * @param diagram train diagram
     * @param type type
     * @param name name
     * @param abbr abbreviation
     */
    Node(String id, TrainDiagram diagram, NodeType type, String name, String abbr) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.abbr = abbr;
        this.diagram = diagram;
        init();
    }
    
    public void addListener(NodeListener listener) {
        this.listenerSupport.addListener(listener);
    }
    
    public void removeListener(NodeListener listener) {
        this.listenerSupport.removeListener(listener);
    }

    /**
     * @return id of the node
     */
    @Override
    public String getId() {
        return id;
    }

    public TrainDiagram getTrainDiagram() {
        return diagram;
    }

    public TimeInterval createTimeInterval(String intervalId, Train train, int start, int stop) {
        int end = start + stop;

        NodeTrack selectedTrack = null;
        TimeInterval interval = new TimeInterval(null, train, this, start, end, null);

        // check which platform is free for adding
        for (NodeTrack nodeTrack : tracks) {
            // skip station tracks with no platform
            if (stop !=0 && train.getType().isPlatform() && !nodeTrack.isPlatform()) {
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

        return new TimeInterval(intervalId, train, this, start, end, selectedTrack);
    }

    /**
     * @return the node tracks
     */
    @Override
    public List<NodeTrack> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void addTrack(NodeTrack track) {
        track.node = this;
        tracks.add(track);
        this.listenerSupport.fireEvent(new NodeEvent(this, NodeEvent.Type.TRACK_ADDED));
    }

    public void addTrack(NodeTrack track, int position) {
        track.node = this;
        tracks.add(position, track);
        this.listenerSupport.fireEvent(new NodeEvent(this, NodeEvent.Type.TRACK_ADDED));
    }

    public void removeTrack(NodeTrack track) {
        track.node = null;
        tracks.remove(track);
        this.listenerSupport.fireEvent(new NodeEvent(this, NodeEvent.Type.TRACK_REMOVED));
    }

    public void removeAllTracks() {
        for (NodeTrack track : tracks) {
            track.node = null;
        }
        tracks.clear();
        this.listenerSupport.fireEvent(new NodeEvent(this, NodeEvent.Type.TRACK_REMOVED));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.listenerSupport.fireEvent(new NodeEvent(this, "name"));
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
        this.listenerSupport.fireEvent(new NodeEvent(this, "abbr"));
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
        this.listenerSupport.fireEvent(new NodeEvent(this, "type"));
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
        this.listenerSupport.fireEvent(new NodeEvent(this, "positionX"));
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
        this.listenerSupport.fireEvent(new NodeEvent(this, "positionY"));
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public Object removeAttribute(String key) {
        Object returnValue = this.attributes.remove(key);
        this.listenerSupport.fireEvent(new NodeEvent(this, key));
        return returnValue;
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
        this.listenerSupport.fireEvent(new NodeEvent(this, key));
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void removeTimeInterval(TimeInterval interval) {
        interval.getTrack().removeTimeInterval(interval);
        this.listenerSupport.fireEvent(new NodeEvent(this, NodeEvent.Type.TIME_INTERVAL_REMOVED));
    }

    @Override
    public void addTimeInterval(TimeInterval interval) {
        interval.getTrack().addTimeInterval(interval);
        this.listenerSupport.fireEvent(new NodeEvent(this, NodeEvent.Type.TIME_INTERVAL_ADDED));
    }

    @Override
    public void updateTimeInterval(TimeInterval interval) {
        Track track = this.getTrackForInterval(interval);
        if (track == null)
            throw new IllegalStateException("Node doesn't contain interval.");
        track.removeTimeInterval(interval);
        interval.getTrack().addTimeInterval(interval);
        this.listenerSupport.fireEvent(new NodeEvent(this, NodeEvent.Type.TIME_INTERVAL_UPDATED));
    }

    private Track getTrackForInterval(TimeInterval interval) {
        for (Track track : getTracks()) {
            if (track.getTimeIntervalList().contains(interval))
                return track;
        }
        return null;
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

    void fireTrackAttributeChanged(String attributeName, NodeTrack track) {
        this.listenerSupport.fireEvent(new NodeEvent(this, attributeName, track));
    }
}
