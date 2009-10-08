package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.model.TrainType;

/**
 * Train type event.
 * 
 * @author jub
 */
public class TrainTypeEvent extends GTEvent<TrainType> {

    public enum Type {

        ATTRIBUTE;
    }
    private Type type;
    private String attributeName;

    public TrainTypeEvent(TrainType trainType, Type type) {
        super(trainType);
        this.type = type;
    }

    public TrainTypeEvent(TrainType trainType, String attributeName) {
        this(trainType, Type.ATTRIBUTE);
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
        StringBuilder builder = new StringBuilder("TrainTypeEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.ATTRIBUTE) {
            builder.append(',').append(attributeName);
        }
        builder.append(']');
        return builder.toString();
    }
}
