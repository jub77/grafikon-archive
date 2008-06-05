/*
 * TrainWrapper.java
 * 
 * Created on 13.9.2007, 8:32:53
 */
package net.parostroj.timetable.gui.views;

import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.utils.TimeConverter;

/**
 * Train wrapper for list of trains.
 * 
 * @author jub
 */
public class TrainWrapper {
    
    public enum Type {
        NAME, NAME_AND_END_NODES, NAME_AND_END_NODES_WITH_TIME;
    }

    private Train train;
    
    private Type type;
    
    public TrainWrapper(Train train, Type type) {
        this.train = train;
        this.type = type;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrainWrapper other = (TrainWrapper) obj;
        if (this.train != other.train && (this.train == null || !this.train.equals(other.train))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.train != null ? this.train.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        switch (type) {
            case NAME: return train.getName();
            case NAME_AND_END_NODES:  return new StringBuilder().append(train.getName()).append(" (").append(train.getStartNode()).append(',').append(train.getEndNode()).append(')').toString();
            case NAME_AND_END_NODES_WITH_TIME:
                return String.format("%s (%s[%s],%s[%s])", train.getName(),train.getStartNode().getName(),TimeConverter.convertFromIntToText(train.getStartTime()),train.getEndNode().getName(),TimeConverter.convertFromIntToText(train.getEndTime()));
            default: return train.getName();
        }
    }
}
