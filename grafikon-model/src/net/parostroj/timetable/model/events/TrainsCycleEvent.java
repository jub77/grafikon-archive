package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.TrainsCycle;
import net.parostroj.timetable.model.TrainsCycleItem;
import net.parostroj.timetable.visitors.EventVisitor;

/**
 * Trains' cycle event.
 * 
 * @author jub
 */
public class TrainsCycleEvent extends GTEvent<TrainsCycle> {

    public enum Type {

        ATTRIBUTE, CYCLE_ITEM_ADDED, CYCLE_ITEM_REMOVED, CYCLE_ITEM_UPDATED, CYCLE_ITEM_COMMENT;
    }
    private Type type;
    private String attributeName;
    private TrainsCycleItem cycleItem;

    public TrainsCycleEvent(TrainsCycle cycle, Type type) {
        super(cycle);
        this.type = type;
    }

    public TrainsCycleEvent(TrainsCycle cycle, Type type, TrainsCycleItem cycleItem) {
        super(cycle);
        this.type = type;
        this.cycleItem = cycleItem;
    }

    public TrainsCycleEvent(TrainsCycle cycle, String attributeName) {
        this(cycle, Type.ATTRIBUTE);
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Type getType() {
        return type;
    }

    public TrainsCycleItem getCycleItem() {
        return cycleItem;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("TrainsCycleEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.ATTRIBUTE) {
            builder.append(',').append(attributeName);
        }
        builder.append(']');
        return builder.toString();
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
