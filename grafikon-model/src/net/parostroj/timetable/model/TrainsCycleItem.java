/*
 * TrainCycleItem.java
 * 
 * Created on 15.9.2007, 19:48:09
 */
package net.parostroj.timetable.model;

/**
 * Train cycle item.
 * 
 * @author jub
 */
public class TrainsCycleItem {

    private Train train;
    private String comment;
    private final TrainsCycle cycle;
    private final Node from;
    private final Node to;

    public TrainsCycleItem(TrainsCycle cycle, Train train, String comment, Node from, Node to) {
        this.cycle = cycle;
        this.train = train;
        this.comment = comment;
        this.from = (train.getStartNode() != from) ? from : null;
        this.to = (train.getEndNode() != to) ? to : null;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Train getTrain() {
        return train;
    }

    public TrainsCycle getCycle() {
        return cycle;
    }

    /**
     * @return from node
     */
    public Node getFrom() {
        return from;
    }

    /**
     * @return to node
     */
    public Node getTo() {
        return to;
    }

    /**
     * @return always returns from node (if not specified then start node of the train)
     */
    public Node getFromNode() {
        if (from != null) {
            return from;
        } else {
            return train.getStartNode();
        }
    }

    /**
     * @return always returns to node (if not specified then end node of the train)
     */
    public Node getToNode() {
        if (to != null) {
            return to;
        } else {
            return train.getEndNode();
        }
    }

    public int getStartTime() {
        Node n = getFrom();
        if (n == null) {
            return train.getStartTime();
        }
        for (TimeInterval interval : train.getTimeIntervalList()) {
            if (interval.getOwner() == n) {
                return interval.getEnd();
            }
        }
        return 0;
    }

    public int getEndTime() {
        Node n = getTo();
        if (n == null) {
            return train.getEndTime();
        }
        for (TimeInterval interval : train.getTimeIntervalList()) {
            if (interval.getOwner() == n) {
                return interval.getStart();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("TrainsCycleItem[");
        builder.append(train).append(',');
        builder.append(getFromNode()).append(',');
        builder.append(getToNode()).append(']');
        return builder.toString();
    }
}
