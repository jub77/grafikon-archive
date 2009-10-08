package net.parostroj.timetable.model.events;

/**
 * Common predecessor for events.
 * 
 * @author jub
 */
public class GTEvent<T> {

    private final T source;
    private final GTEvent<?> nestedEvent;
    
    public GTEvent(T source) {
        this.source = source;
        this.nestedEvent = null;
    }
    
    public GTEvent(T source, GTEvent<?> nestedEvent) {
        this.source = source;
        this.nestedEvent = nestedEvent;
    }

    public T getSource() {
        return source;
    }

    public GTEvent<?> getNestedEvent() {
        return nestedEvent;
    }
}
