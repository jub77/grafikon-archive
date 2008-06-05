package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Train;

/**
 * Train event.
 * 
 * @author jub
 */
public class TrainEvent extends GTEvent<Train> {

    public enum Type {

        ATTRIBUTE_CHANGED, TIME_CHANGED;
    }
    private Type type;
    private String attributeName;
    private Object attributeValue;

    public TrainEvent(Train train, Type type) {
        super(train);
        this.type = type;
    }
    
    public TrainEvent(Train train, String attributeName, Object attributeValue) {
        this(train, Type.ATTRIBUTE_CHANGED);
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Object getAttributeValue() {
        return attributeValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("TrainEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.ATTRIBUTE_CHANGED) {
            builder.append(',').append(attributeName).append(',').append(attributeValue);
        }
        builder.append(']');
        return builder.toString();
    }
}
