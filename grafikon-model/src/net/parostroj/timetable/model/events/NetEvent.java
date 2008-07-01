package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Net;

/**
 * Net event.
 * 
 * @author jub
 */
public class NetEvent extends GTEvent<Net> {

    public enum Type {

        NODE_ADDED, NODE_REMOVED, LINE_ADDED, LINE_REMOVED, LINE_CLASS_ADDED, LINE_CLASS_REMOVED
    }
    private final Type type;

    public NetEvent(Net net, Type type) {
        super(net);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("NetEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        builder.append(']');
        return builder.toString();
    }
}
