package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Line;
import net.parostroj.timetable.model.Node;

/**
 * Line event.
 * 
 * @author jub
 */
public class LineEvent extends GTEvent<Line> {

    public enum Type {
        ATTRIBUTE, TRACK_ADDED, TRACK_REMOVED, TIME_INTERVAL_ADDED, TIME_INTERVAL_REMOVED
    }
    private final Type type;
    private final String attributeName;

    public LineEvent(Line line, Type type) {
        super(line);
        this.type = type;
        this.attributeName = null;
    }
    
    public LineEvent(Line line, String attributeName) {
        super(line);
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
        StringBuilder builder = new StringBuilder("LineEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.ATTRIBUTE) {
            builder.append(',').append(attributeName);
        }
        builder.append(']');
        return builder.toString();
    }
}
