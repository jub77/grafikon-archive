/*
 * TrainCycleItem.java
 * 
 * Created on 15.9.2007, 19:48:09
 */
package net.parostroj.timetable.model;

import net.parostroj.timetable.model.events.TrainEvent;

/**
 * Train cycle item.
 * 
 * @author jub
 */
public class TrainsCycleItem {

    private Train train;
    private String comment;
    private final TrainsCycle cycle;
    private final TimeInterval from;
    private final TimeInterval to;

    public TrainsCycleItem(TrainsCycle cycle, Train train, String comment, TimeInterval from, TimeInterval to) {
        this.cycle = cycle;
        this.train = train;
        this.comment = comment;
        this.from = (train.getFirstInterval() != from) ? from : null;
        this.to = (train.getLastInterval() != to) ? to : null;
    }
    
    public boolean containsInterval(TimeInterval interval) {
        boolean in = false;
        for (TimeInterval currentInterval : train.getTimeIntervalList()) {
            if (getFromInterval() == currentInterval)
                in = true;
            if (in && interval == currentInterval)
                return true;
            if (getToInterval() == currentInterval)
                in = false;
        }
        return false;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        train.fireTrainEvent(new TrainEvent(train, "trains.cycle.item.comment"));
    }

    public Train getTrain() {
        return train;
    }

    public TrainsCycle getCycle() {
        return cycle;
    }

    /**
     * @return from time interval
     */
    public TimeInterval getFrom() {
        return from;
    }

    /**
     * @return to interval
     */
    public TimeInterval getTo() {
        return to;
    }

    /**
     * @return always returns from time interval (if not specified then firt interval of the train)
     */
    public TimeInterval getFromInterval() {
        if (from != null) {
            return from;
        } else {
            return train.getFirstInterval();
        }
    }

    /**
     * @return always returns to interval (if not specified then last interval of the train)
     */
    public TimeInterval getToInterval() {
        if (to != null) {
            return to;
        } else {
            return train.getLastInterval();
        }
    }

    public int getStartTime() {
        return this.getFromInterval().getEnd();
    }

    public int getEndTime() {
        return this.getToInterval().getStart();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("TrainsCycleItem[");
        builder.append(train).append(',');
        builder.append(getFromInterval().getOwner()).append(',');
        builder.append(getToInterval().getOwner()).append(']');
        return builder.toString();
    }
}
