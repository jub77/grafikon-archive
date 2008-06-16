package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.TrainDiagram;

/**
 * Train diagram event.
 * 
 * @author jub
 */
public class TrainDiagramEvent extends GTEvent<TrainDiagram> {

    public enum Type {
    }
    private Type type;
    private String attributeName;

    public TrainDiagramEvent(TrainDiagram diagram, Type type) {
        super(diagram);
        this.type = type;
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
