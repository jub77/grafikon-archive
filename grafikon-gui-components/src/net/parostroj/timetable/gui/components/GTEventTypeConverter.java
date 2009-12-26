package net.parostroj.timetable.gui.components;

import net.parostroj.timetable.model.events.GTEvent;

/**
 * GTEvent type converter for Events Viewer.
 *
 * @author jub
 */
public class GTEventTypeConverter implements EventsViewerTypeConverter {

    @Override
    public String getListString(Object event) {
        return event.toString();
    }

    @Override
    public String getViewString(Object event) {
        return event.toString();
    }

    @Override
    public Class<?> getEventClass() {
        return GTEvent.class;
    }
}
