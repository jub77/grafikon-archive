package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.NodeTrack;

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

    public NodeEvent(Node node, Type type) {
        super(node);
        this.type = type;
        this.attributeName = null;
        this.track = null;
    }
    
    public NodeEvent(Node node, String attributeName) {
        super(node);
        this.type = Type.ATTRIBUTE;
        this.attributeName = attributeName;
        this.track = null;
    }
    
    public NodeEvent(Node node, String attributeName, NodeTrack track) {
        super(node);
        this.type = Type.TRACK_ATTRIBUTE;
        this.attributeName = attributeName;
        this.track = track;
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
        builder.append(']');
        return builder.toString();
    }
}
