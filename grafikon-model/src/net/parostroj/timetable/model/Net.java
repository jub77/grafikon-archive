package net.parostroj.timetable.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import net.parostroj.timetable.utils.Tuple;
import org.jgrapht.graph.ListenableUndirectedGraph;

/**
 * Special class for net.
 *
 * @author jub
 */
public class Net extends ListenableUndirectedGraph<Node, Line> {
    
    private List<LineClass> lineClasses;

    /**
     * Constructor.
     */
    public Net() {
        super(Line.class);
        lineClasses = new LinkedList<LineClass>();
    }
    
    public Tuple<Node> getNodes(Line track) {
        return new Tuple<Node>(this.getEdgeSource(track),this.getEdgeTarget(track));
    }

    public List<LineClass> getLineClasses() {
        return Collections.unmodifiableList(lineClasses);
    }
    
    public void addLineClass(LineClass lineClass) {
        lineClasses.add(lineClass);
    }
    
    public void addLineClass(LineClass lineClass, int position) {
        lineClasses.add(position, lineClass);
    }
    
    public void removeLineClass(LineClass lineClass) {
        lineClasses.remove(lineClass);
    }
    
    public Node getNodeById(String id) {
        for (Node node : vertexSet()) {
            if (node.getId().equals(id))
                return node;
        }
        return null;
    }
    
    public Line getLineById(String id) {
        for (Line line : edgeSet()) {
            if (line.getId().equals(id))
                return line;
        }
        return null;
    }
    
    public LineClass getLineClassById(String id) {
        for (LineClass lineClass : getLineClasses()) {
            if (lineClass.getId().equals(id))
                return lineClass;
        }
        return null;
    }
}
