package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.model.TrainsCycleItem;

/**
 * Train event.
 * 
 * @author jub
 */
public class TrainEvent extends GTEvent<Train> {

    public static enum Type {
        ATTRIBUTE, TIME_INTERVAL_LIST, CYCLE_ITEM_ADDED, CYCLE_ITEM_REMOVED, TECHNOLOGICAL;
    }

    public static enum TimeIntervalListType {
        MOVED, STOP_TIME, SPEED, TRACK, RECALCULATE, ADDED;
    }

    private Type type;
    private TimeIntervalListType timeIntervalListType;
    private String attributeName;
    private TrainsCycleItem cycleItem;
    private int intervalChangeStart;
    private int changedInterval;

    public TrainEvent(Train train, Type type) {
        super(train);
        this.type = type;
    }

    public TrainEvent(Train train, String attributeName) {
        this(train, Type.ATTRIBUTE);
        this.attributeName = attributeName;
    }

    public TrainEvent(Train train, Type type, TrainsCycleItem cycleItem) {
        this(train, type);
        this.cycleItem = cycleItem;
    }

    public TrainEvent(Train train, TimeIntervalListType type, int changedInterval, int intervalChangeStart) {
        this(train, Type.TIME_INTERVAL_LIST);
        this.timeIntervalListType = type;
        this.changedInterval = changedInterval;
        this.intervalChangeStart = intervalChangeStart;
    }

    public TrainEvent(Train train, TimeIntervalListType type, int changedInterval) {
        this(train, type, changedInterval, 0);
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

    public int getChangedInterval() {
        return changedInterval;
    }

    public int getIntervalChangeStart() {
        return intervalChangeStart;
    }

    public TimeIntervalListType getTimeIntervalListType() {
        return timeIntervalListType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("TrainEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.ATTRIBUTE) {
            builder.append(',').append(attributeName);
        }
        if (cycleItem != null) {
            builder.append(',').append(cycleItem);
        }
        builder.append(',').append(changedInterval).append(',').append(intervalChangeStart);
        builder.append(']');
        return builder.toString();
    }
}
