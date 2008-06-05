package net.parostroj.timetable.model.events;

/**
 * Common predecessor for events.
 * 
 * @author jub
 */
public class GTEvent<T> {

    private final T source;
    
    public GTEvent(T source) {
        this.source = source;
    }

    public T getSource() {
        return source;
    }
}
