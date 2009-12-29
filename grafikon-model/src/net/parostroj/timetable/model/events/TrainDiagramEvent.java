package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Route;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.model.TrainType;

/**
 * Train diagram event.
 * 
 * @author jub
 */
public class TrainDiagramEvent extends GTEvent<TrainDiagram> {

    public enum Type {
        NESTED, ROUTE_ADDED, ROUTE_REMOVED, TRAIN_ADDED, TRAIN_REMOVED,
        TRAIN_TYPE_ADDED, TRAIN_TYPE_REMOVED, ATTRIBUTE;
    }
    private Type type;
    private String attributeName;
    private Route route;
    private Train train;
    private TrainType trainType;

    public TrainDiagramEvent(TrainDiagram diagram, Type type) {
        super(diagram);
        this.type = type;
    }
    
    public TrainDiagramEvent(TrainDiagram diagram, GTEvent<?> event) {
        super(diagram, event);
        this.type = Type.NESTED;
    }

    public TrainDiagramEvent(TrainDiagram diagram, Type type, Route route) {
        super(diagram);
        this.type = type;
        this.route = route;
    }

    public TrainDiagramEvent(TrainDiagram diagram, Type type, Train train) {
        super(diagram);
        this.type = type;
        this.train = train;
    }

    public TrainDiagramEvent(TrainDiagram diagram, Type type, TrainType trainType) {
        super(diagram);
        this.type = type;
        this.trainType = trainType;
    }

    public TrainDiagramEvent(TrainDiagram diagram, String attributeName) {
        this(diagram, Type.ATTRIBUTE);
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Type getType() {
        return type;
    }

    public Route getRoute() {
        return route;
    }

    public Train getTrain() {
        return train;
    }

    public TrainType getTrainType() {
        return trainType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("TrainDiagramEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        builder.append(']');
        return builder.toString();
    }
}
