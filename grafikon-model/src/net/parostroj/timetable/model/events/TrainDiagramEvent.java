package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.TrainDiagram;

/**
 * Train diagram event.
 * 
 * @author jub
 */
public class TrainDiagramEvent extends GTEvent<TrainDiagram> {

    public enum Type {
        NESTED, ROUTE_ADDED, ROUTE_REMOVED, TRAIN_ADDED, TRAIN_REMOVED
    }
    private Type type;
    private String attributeName;

    public TrainDiagramEvent(TrainDiagram diagram, Type type) {
        super(diagram);
        this.type = type;
    }
    
    public TrainDiagramEvent(TrainDiagram diagram, GTEvent<?> event) {
        super(diagram, event);
        this.type = Type.NESTED;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Type getType() {
        return type;
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
