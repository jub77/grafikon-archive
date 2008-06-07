package net.parostroj.timetable.actions;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.Tuple;

/**
 * Class for managing lists of trains cycle items.
 * 
 * @author jub
 */
public class TrainsCycleHelper {
    
    private final static TrainsCycleHelper instance = new TrainsCycleHelper();

    private TrainsCycleHelper() {}
    
    public static TrainsCycleHelper getHelper(TrainsCycleType type) {
        // currently one type of helper for all types
        return instance;
    }
    
    public boolean isTrainCovered(Train train, List<TrainsCycleItem> items) {
        if (items == null)
            throw new IllegalArgumentException("List cannot be null.");
        if (items.isEmpty())
            return false;
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        TrainsCycleItem si = fi;
        if (train.getStartNode() != fi.getFromNode())
            return false;
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToNode() != si.getFromNode())
                return false;
            fi = si;
        }
        return si.getToNode() == train.getEndNode();
    }
    
    public Tuple<Node> getFirstUncoveredPart(Train train, List<TrainsCycleItem> items) {
        if (items == null)
            throw new IllegalArgumentException("List cannot be null.");
        if (items.isEmpty())
            return new Tuple<Node>(train.getStartNode(), train.getEndNode());
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        TrainsCycleItem si = fi;
        if (fi.getFromNode() != train.getStartNode())
            return new Tuple<Node>(train.getStartNode(), fi.getFromNode());
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToNode() != si.getFromNode())
                return new Tuple<Node>(fi.getToNode(), si.getFromNode());
            fi = si;
        }
        if (si.getToNode() != train.getEndNode()) {
            return new Tuple<Node>(si.getTo(), train.getEndNode());
        } else {
            return new Tuple<Node>(null, null);
        }
    }

    public List<Tuple<Node>> getAllUncoveredParts(Train train, List<TrainsCycleItem> items) {
        if (items == null)
            throw new IllegalArgumentException("List cannot be null.");
        if (items.isEmpty()) {
            return Collections.singletonList(new Tuple<Node>(train.getStartNode(), train.getEndNode()));
        }
        List<Tuple<Node>> result = new LinkedList<Tuple<Node>>();
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        TrainsCycleItem si = fi;
        if (fi.getFromNode() != train.getStartNode())
            result.add(new Tuple<Node>(train.getStartNode(), fi.getFromNode()));
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToNode() != si.getFromNode())
                result.add(new Tuple<Node>(fi.getToNode(), si.getFromNode()));
            fi = si;
        }
        if (si.getToNode() != train.getEndNode()) {
            result.add(new Tuple<Node>(si.getTo(), train.getEndNode()));
        }
        return result;
    }
    
    public List<List<Node>> getAllUncoveredLists(Train train, List<TrainsCycleItem> items) {
        List<Tuple<Node>> tuples = this.getAllUncoveredParts(train, items);
        List<List<Node>> result = new LinkedList<List<Node>>();
        for (Tuple<Node> tuple : tuples) {
            List<Node> nodes = new LinkedList<Node>();
            boolean collect = false;
            for (TimeInterval interval : train.getTimeIntervalList()) {
                if (interval.getOwner() instanceof Node) {
                    Node node = interval.getOwner().asNode();
                    if (node == tuple.first)
                        collect = true;
                    if (collect)
                        nodes.add(node);
                    if (node == tuple.second)
                        collect = false;
                }
            }
            result.add(nodes);
        }
        return result;
    }
}
