package net.parostroj.timetable.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.parostroj.timetable.utils.Tuple;
import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.ListenableUndirectedGraph;

/**
 * Special class for net.
 *
 * @author jub
 */
public class Net {
    
    private List<LineClass> lineClasses;
    ListenableUndirectedGraph<Node, Line> netDelegate;

    /**
     * Constructor.
     */
    public Net() {
        netDelegate = new ListenableUndirectedGraph<Node, Line>(Line.class);
        lineClasses = new LinkedList<LineClass>();
    }
    
    public Tuple<Node> getNodes(Line track) {
        return new Tuple<Node>(netDelegate.getEdgeSource(track),netDelegate.getEdgeTarget(track));
    }
    
    public Set<Node> getNodes() {
        return netDelegate.vertexSet();
    }
    
    public void addNode(Node node) {
        netDelegate.addVertex(node);
    }
    
    public void removeNode(Node node) {
        netDelegate.removeVertex(node);
    }
    
    public Set<Line> getLines() {
        return netDelegate.edgeSet();
    }
    
    public Set<Line> getLinesOf(Node node) {
        return netDelegate.edgesOf(node);
    }
    
    public void addLine(Node from, Node to, Line line) {
        netDelegate.addEdge(from, to, line);
    }
    
    public void removeLine(Line line) {
        netDelegate.removeEdge(line);
    }
    
    public List<Line> getRoute(Node from, Node to) {
        return DijkstraShortestPath.findPathBetween(netDelegate, from, to);
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
        for (Node node : netDelegate.vertexSet()) {
            if (node.getId().equals(id))
                return node;
        }
        return null;
    }
    
    public Line getLineById(String id) {
        for (Line line : netDelegate.edgeSet()) {
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
    
    public Graph<Node, Line> getGraph() {
        return netDelegate;
    }
}
