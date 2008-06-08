package net.parostroj.timetable.actions;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.Pair;
import net.parostroj.timetable.utils.Tuple;

/**
 * Class for managing lists of trains cycle items.
 * 
 * @author jub
 */
public class TrainsCycleHelper {

    private final static TrainsCycleHelper instance = new TrainsCycleHelper();

    private TrainsCycleHelper() {
    }

    public static TrainsCycleHelper getHelper(TrainsCycleType type) {
        // currently one type of helper for all types
        return instance;
    }

    public void addCycleItem(Train train, List<TrainsCycleItem> items, TrainsCycleItem item) {
        if (!checkNodes(train, item))
            throw new IllegalArgumentException("Invalid item.");
        if (!testAddCycle(train, items, item, null))
            throw new IllegalArgumentException("Overlapping item.");
        ListIterator<TrainsCycleItem> i = items.listIterator();
        TrainsCycleItem current = getNext(i, null);
        for (TimeInterval interval : train.getTimeIntervalList()) {
            if (interval.isNodeOwner()) {
                Node node = interval.getOwnerAsNode();
                if (current != null && node == current.getFromNode())
                    current = getNext(i, null);
                if (current == null || node == item.getFromNode()) {
                    if (current != null && i.hasPrevious())
                        i.previous();
                    i.add(item);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Cannot include item: " + item);
    }

    public boolean isTrainCovered(Train train, List<TrainsCycleItem> items) {
        if (items == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        if (items.isEmpty()) {
            return false;
        }
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        TrainsCycleItem si = fi;
        if (train.getStartNode() != fi.getFromNode()) {
            return false;
        }
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToNode() != si.getFromNode()) {
                return false;
            }
            fi = si;
        }
        return si.getToNode() == train.getEndNode();
    }

    public Tuple<Node> getFirstUncoveredPart(Train train, List<TrainsCycleItem> items) {
        if (items == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        if (items.isEmpty()) {
            return new Tuple<Node>(train.getStartNode(), train.getEndNode());
        }
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        TrainsCycleItem si = fi;
        if (fi.getFromNode() != train.getStartNode()) {
            return new Tuple<Node>(train.getStartNode(), fi.getFromNode());
        }
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToNode() != si.getFromNode()) {
                return new Tuple<Node>(fi.getToNode(), si.getFromNode());
            }
            fi = si;
        }
        if (si.getToNode() != train.getEndNode()) {
            return new Tuple<Node>(si.getTo(), train.getEndNode());
        } else {
            return new Tuple<Node>(null, null);
        }
    }

    public List<Tuple<Node>> getAllUncoveredParts(Train train, List<TrainsCycleItem> items) {
        if (items == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        if (items.isEmpty()) {
            return Collections.singletonList(new Tuple<Node>(train.getStartNode(), train.getEndNode()));
        }
        List<Tuple<Node>> result = new LinkedList<Tuple<Node>>();
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        TrainsCycleItem si = fi;
        if (fi.getFromNode() != train.getStartNode()) {
            result.add(new Tuple<Node>(train.getStartNode(), fi.getFromNode()));
        }
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToNode() != si.getFromNode()) {
                result.add(new Tuple<Node>(fi.getToNode(), si.getFromNode()));
            }
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
                    if (node == tuple.first) {
                        collect = true;
                    }
                    if (collect) {
                        nodes.add(node);
                    }
                    if (node == tuple.second) {
                        collect = false;
                    }
                }
            }
            result.add(nodes);
        }
        return result;
    }

    public List<Pair<RouteSegment, Boolean>> getRouteCoverage(Train train, List<TrainsCycleItem> items) {
        List<Pair<RouteSegment, Boolean>> result = new LinkedList<Pair<RouteSegment, Boolean>>();
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem current = i.hasNext() ? i.next() : null;
        boolean in = false;
        for (TimeInterval interval : train.getTimeIntervalList()) {
            Pair<RouteSegment, Boolean> pair = new Pair<RouteSegment, Boolean>(interval.getOwner(), Boolean.FALSE);
            if (pair.first instanceof Node) {
                if (current != null && pair.first == current.getFromNode()) {
                    in = true;
                }
                if (in) {
                    pair.second = true;
                }
                if (current != null && pair.first == current.getToNode()) {
                    in = false;
                    current = i.hasNext() ? i.next() : null;
                    if (current != null && pair.first == current.getFromNode()) {
                        in = true;
                    }
                }
            } else {
                if (in) {
                    pair.second = true;
                }
            }

            result.add(pair);
        }
        return result;
    }

    public boolean testAddCycle(Train train, List<TrainsCycleItem> items, TrainsCycleItem newItem, TrainsCycleItem ignoredItem) {
        if (!checkNodes(train, newItem)) {
            return false;
        }
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem ftci = getNext(i, ignoredItem);
        boolean inserted = false;
        boolean occupied = false;
        for (TimeInterval interval : newItem.getTrain().getTimeIntervalList()) {
            if (ftci == null) {
                return true;
            }
            if (interval.getOwner() instanceof Node) {
                Node node = (Node) interval.getOwner();
                if (ftci.getFromNode() == node) {
                    occupied = true;
                }
                if (newItem.getFromNode() == node) {
                    inserted = true;
                }
                if (newItem.getToNode() == node) {
                    inserted = false;
                }
                if (ftci.getToNode() == node) {
                    occupied = false;
                    ftci = getNext(i, ignoredItem);
                    if (ftci != null && ftci.getFromNode() == node) {
                        occupied = true;
                    }
                }
                if (occupied && inserted) {
                    return false;
                }
            }
        }
        return true;
    }

    private TrainsCycleItem getNext(Iterator<TrainsCycleItem> i, TrainsCycleItem ignored) {
        while (i.hasNext()) {
            TrainsCycleItem item = i.next();
            if (item != ignored) {
                return item;
            }
        }
        return null;
    }

    private boolean checkNodes(Train train, TrainsCycleItem item) {
        if (item.getFromNode() == item.getToNode()) {
            return false;
        }
        boolean in = false;
        for (TimeInterval interval : train.getTimeIntervalList()) {
            if (interval.isNodeOwner()) {
                Node node = interval.getOwnerAsNode();
                if (!in && (node == item.getFromNode())) {
                    in = true;
                }
                if (node == item.getToNode()) {
                    return in;
                }
            }
        }
        return false;
    }
}
