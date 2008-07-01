package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Node;

/**
 * Node event.
 * 
 * @author jub
 */
public class NodeEvent extends GTEvent<Node> {

    public enum Type {
        ATTRIBUTE, TRACK_ADDED, TRACK_REMOVED, TIME_INTERVAL_ADDED, TIME_INTERVAL_REMOVED
    }
    private final Type type;
    private final String attributeName;

    public NodeEvent(Node node, Type type) {
        super(node);
        this.type = type;
        this.attributeName = null;
    }
    
    public NodeEvent(Node node, String attributeName) {
        super(node);
        this.type = Type.ATTRIBUTE;
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("NodeEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.ATTRIBUTE) {
            builder.append(',').append(attributeName);
        }
        builder.append(']');
        return builder.toString();
    }
}
