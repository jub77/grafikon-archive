package net.parostroj.timetable.model.events;

import net.parostroj.timetable.model.Line;
import net.parostroj.timetable.model.LineClass;
import net.parostroj.timetable.model.Net;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.visitors.EventVisitor;

/**
 * Net event.
 * 
 * @author jub
 */
public class NetEvent extends GTEvent<Net> {

    public enum Type {
        NODE_ADDED, NODE_REMOVED, LINE_ADDED, LINE_REMOVED, LINE_CLASS_ADDED, LINE_CLASS_REMOVED, NESTED
    }

    private final Type type;
    private Node node;
    private Line line;
    private LineClass lineClass;

    public NetEvent(Net net, Type type, Node node) {
        super(net);
        this.type = type;
        this.node = node;
    }

    public NetEvent(Net net, Type type, Line line) {
        super(net);
        this.type = type;
        this.line = line;
    }

    public NetEvent(Net net, Type type, LineClass lineClass) {
        super(net);
        this.type = type;
        this.lineClass = lineClass;
    }

    public NetEvent(Net net, GTEvent<?> event) {
        super(net, event);
        this.type = Type.NESTED;
    }

    public Type getType() {
        return type;
    }

    public Line getLine() {
        return line;
    }

    public LineClass getLineClass() {
        return lineClass;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("NetEvent[");
        builder.append(getSource()).append(',');
        builder.append(type);
        if (type == Type.NESTED) {
            builder.append(',').append(getNestedEvent());
        }
        builder.append(']');
        return builder.toString();
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
