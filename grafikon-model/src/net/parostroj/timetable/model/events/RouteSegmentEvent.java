package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.model.Track;

/**
 * Line event.
 * 
 * @author jub
 */
public abstract class RouteSegmentEvent<T, U extends Track> extends GTEvent<T> {

    private U track;
    private TimeInterval interval;

    public RouteSegmentEvent(T segment, GTEventType type) {
        super(segment, type);
    }
    
    public RouteSegmentEvent(T segment, GTEventType type, U track) {
        super(segment, type);
        this.track = track;
    }

    public RouteSegmentEvent(T segment, AttributeChange attributeChange) {
        super(segment, GTEventType.ATTRIBUTE);
        setAttributeChange(attributeChange);
    }
    
    public RouteSegmentEvent(T segment, AttributeChange attributeChange, U track) {
        super(segment, GTEventType.TRACK_ATTRIBUTE);
        setAttributeChange(attributeChange);
        this.track = track;
    }

    public RouteSegmentEvent(T segment, GTEventType type, TimeInterval interval) {
        super(segment, type);
        this.interval = interval;
    }

    public U getTrack() {
        return track;
    }

    public TimeInterval getInterval() {
        return interval;
    }
}
