package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Line;
import net.parostroj.timetable.model.LineTrack;

/**
 * Line event.
 * 
 * @author jub
 */
public class LineEvent extends GTEvent<Line> {

    public enum Type {
        ATTRIBUTE, TRACK_ADDED, TRACK_REMOVED, TIME_INTERVAL_ADDED, TIME_INTERVAL_REMOVED, TIME_INTERVAL_UPDATED, TRACK_ATTRIBUTE
    }
    private final Type type;
    private final String attributeName;
    private final LineTrack track;

    public LineEvent(Line line, Type type) {
        super(line);
        this.type = type;
        this.attributeName = null;
        this.track = null;
    }
    
    public LineEvent(Line line, String attributeName) {
        super(line);
        this.type = Type.ATTRIBUTE;
        this.attributeName = attributeName;
        this.track = null;
    }
    
    public LineEvent(Line line, String attributeName, LineTrack track) {
        super(line);
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

    public LineTrack getTrack() {
        return track;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("LineEvent[");
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
