/*
 * TrainWrapper.java
 * 
 * Created on 13.9.2007, 8:32:53
 */
package net.parostroj.timetable.gui.helpers;

import net.parostroj.timetable.actions.TrainComparator;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.utils.TimeConverter;

/**
 * Train wrapper for list of trains.
 * 
 * @author jub
 */
public class TrainWrapper extends Wrapper<Train> {
    
    public enum Type {
        NAME, NAME_AND_END_NODES, NAME_AND_END_NODES_WITH_TIME;
    }

    private Type type;
    private TrainComparator comparator;
    
    public TrainWrapper(Train train, Type type) {
        super(train);
        this.type = type;
    }

    public TrainWrapper(Train train, Type type, TrainComparator comparator) {
        this(train, type);
        this.comparator = comparator;
    }

    public Train getTrain() {
        return this.getElement();
    }

    public void setTrain(Train train) {
        this.setElement(train);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        switch (type) {
            case NAME:
                return getTrain().getName();
            case NAME_AND_END_NODES:
                return String.format("%s (%s,%s)",
                        getTrain().getName(),
                        getTrain().getStartNode(),
                        getTrain().getEndNode());
            case NAME_AND_END_NODES_WITH_TIME:
                return String.format("%s (%s[%s],%s[%s])",
                        getTrain().getName(),
                        getTrain().getStartNode().getName(),
                        TimeConverter.convertFromIntToText(getTrain().getStartTime()),
                        getTrain().getEndNode().getName(),
                        TimeConverter.convertFromIntToText(getTrain().getEndTime()));
            default:
                return getTrain().getName();
        }
    }

    @Override
    public int compareTo(Wrapper<Train> o) {
        if (comparator == null)
            return super.compareTo(o);
        else
            return comparator.compare(this.getElement(), o.getElement());
    }
}
