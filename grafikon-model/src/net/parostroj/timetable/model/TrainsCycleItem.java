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
    
    private Node from;
    
    private Node to;

    public TrainsCycleItem(TrainsCycle cycle, Train train, String comment, Node from, Node to) {
        this(cycle, train, comment);
        this.from = from;
        this.to = to;
    }

    public TrainsCycleItem(TrainsCycle cycle, Train train, String comment) {
        this.train = train;
        this.comment = comment;
        this.cycle = cycle;
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

    public void setTrain(Train train) {
        this.train = train;
    }

    public TrainsCycle getCycle() {
        return cycle;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }
    
    public Node getFromNode() {
        if (from != null)
            return from;
        else
            return train.getStartNode();
    }
    
    public Node getToNode() {
        if (to != null)
            return to;
        else
            return train.getEndNode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrainsCycleItem other = (TrainsCycleItem) obj;
        if (this.train != other.train && (this.train == null || !this.train.equals(other.train))) {
            return false;
        }
        if (this.comment != other.comment && (this.comment == null || !this.comment.equals(other.comment))) {
            return false;
        }
        if (this.cycle != other.cycle && (this.cycle == null || !this.cycle.equals(other.cycle))) {
            return false;
        }
        if (this.from != other.from && (this.from == null || !this.from.equals(other.from))) {
            return false;
        }
        if (this.to != other.to && (this.to == null || !this.to.equals(other.to))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.train != null ? this.train.hashCode() : 0);
        hash = 23 * hash + (this.comment != null ? this.comment.hashCode() : 0);
        return hash;
    }
    
    
}
