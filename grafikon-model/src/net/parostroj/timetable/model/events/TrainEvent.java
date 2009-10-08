package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Train;

/**
 * Train event.
 * 
 * @author jub
 */
public class TrainEvent extends GTEvent<Train> {

    public enum Type {

        ATTRIBUTE, TIME_INTERVAL_LIST, CYCLE_ITEM, TECHNOLOGICAL;
    }
    private Type type;
    private String attributeName;

    public TrainEvent(Train train, Type type) {
        super(train);
        this.type = type;
    }

    public TrainEvent(Train train, String attributeName) {
        this(train, Type.ATTRIBUTE);
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
        StringBuilder builder = new StringBuilder("TrainEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.ATTRIBUTE) {
            builder.append(',').append(attributeName);
        }
        builder.append(']');
        return builder.toString();
    }
}
