package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.NodeTrack;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.visitors.EventVisitor;

/**
 * Node event.
 * 
 * @author jub
 */
public class NodeEvent extends GTEvent<Node> {

    public enum Type {
        ATTRIBUTE, TRACK_ADDED, TRACK_REMOVED, TIME_INTERVAL_ADDED, TIME_INTERVAL_REMOVED, TIME_INTERVAL_UPDATED, TRACK_ATTRIBUTE
    }
    private final Type type;
    private final String attributeName;
    private final NodeTrack track;
    private final TimeInterval interval;

    public NodeEvent(Node node, Type type) {
        super(node);
        this.type = type;
        this.attributeName = null;
        this.track = null;
        this.interval = null;
    }
    
    public NodeEvent(Node node, String attributeName) {
        super(node);
        this.type = Type.ATTRIBUTE;
        this.attributeName = attributeName;
        this.track = null;
        this.interval = null;
    }
    
    public NodeEvent(Node node, String attributeName, NodeTrack track) {
        super(node);
        this.type = Type.TRACK_ATTRIBUTE;
        this.attributeName = attributeName;
        this.track = track;
        this.interval = null;
    }

    public NodeEvent(Node node, Type type, TimeInterval interval) {
        super(node);
        this.type = type;
        this.attributeName = null;
        this.track = null;
        this.interval = interval;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Type getType() {
        return type;
    }

    public NodeTrack getTrack() {
        return track;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("NodeEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.ATTRIBUTE || type == Type.TRACK_ATTRIBUTE) {
            builder.append(',').append(attributeName);
        }
        if (track != null) {
            builder.append(',').append(track);
        }
        if (interval != null) {
            builder.append(',').append(interval);
        }
        builder.append(']');
        return builder.toString();
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
