package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Line;
import net.parostroj.timetable.model.LineTrack;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.visitors.EventVisitor;

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
    private final TimeInterval interval;

    public LineEvent(Line line, Type type) {
        super(line);
        this.type = type;
        this.attributeName = null;
        this.track = null;
        this.interval = null;
    }
    
    public LineEvent(Line line, String attributeName) {
        super(line);
        this.type = Type.ATTRIBUTE;
        this.attributeName = attributeName;
        this.track = null;
        this.interval = null;
    }
    
    public LineEvent(Line line, String attributeName, LineTrack track) {
        super(line);
        this.type = Type.TRACK_ATTRIBUTE;
        this.attributeName = attributeName;
        this.track = track;
        this.interval = null;
    }

    public LineEvent(Line line, Type type, TimeInterval interval) {
        super(line);
        this.type = type;
        this.interval = interval;
        this.track = null;
        this.attributeName = null;
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

    public TimeInterval getInterval() {
        return interval;
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
