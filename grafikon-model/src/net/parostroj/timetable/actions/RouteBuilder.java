package net.parostroj.timetable.actions;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import net.parostroj.timetable.model.*;
import org.jgrapht.alg.DijkstraShortestPath;

/**
 * Builder for creating routes. It uses Dijkstra algorithm for finding
 * shortest path in the graph.
 * 
 * @author jub
 */
public class RouteBuilder {
    /**
     * creates instence.
     */
    public RouteBuilder() {
    }

    /**
     * creates route through all nodes in the list.
     * 
     * @param id id
     * @param net net
     * @param nodes list of nodes
     * @return route
     */
    public Route createRoute(String id, Net net, List<Node> nodes) {
        if (nodes == null || nodes.size() < 2)
            throw new IllegalArgumentException("Not enough nodes.");
        ListIterator<Node> i = nodes.listIterator();
        Node last = i.next();
        Route route = new Route(id);
        while (i.hasNext()) {
            Node current = i.next();
            Route part = this.createRouteInternal(id, net, last, current);
            route.add(part);
            last = current;
        }
        return route;
    }
    
    /**
     * creates route through all nodes in the list
     * 
     * @param id id
     * @param net net
     * @param nodes nodes
     * @return route
     */
    public Route createRoute(String id, Net net, Node... nodes) {
        List<Node> nList = Arrays.asList(nodes);
        return this.createRoute(id, net, nList);
    }

    /**
     * creates route from point A to point B.
     * 
     * @param id id
     * @param net net
     * @param from point A
     * @param to point B
     * @return route
     */
    private Route createRouteInternal(String id, Net net, Node from, Node to) {
        List<Line> lines = DijkstraShortestPath.findPathBetween(net, from, to);
        // create route
        Route route = new Route(id);
        // last route point
        Node lastNode = from;
        for (Line line : lines) {
            Node node1 = net.getEdgeSource(line);
            Node node2 = net.getEdgeTarget(line);
            Node fromNode = (lastNode == node1) ? node1 : node2;
            Node toNode = (lastNode == node1) ? node2 : node1;
            route.getSegments().add(fromNode);
            route.getSegments().add(line);
            lastNode = toNode;
        }
        route.getSegments().add(lastNode);
        return route;
    }
}
