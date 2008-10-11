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
                if (current != null && interval == current.getFromInterval())
                    current = getNext(i, null);
                if (current == null || interval == item.getFromInterval()) {
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
        if (train.getFirstInterval() != fi.getFromInterval()) {
            return false;
        }
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToInterval() != si.getFromInterval()) {
                return false;
            }
            fi = si;
        }
        return si.getToInterval() == train.getLastInterval();
    }
    
    public boolean isTrainIntervalCovered(Train train, List<TrainsCycleItem> items, TimeInterval interval) {
        if (items == null)
            throw new IllegalArgumentException("List cannot be null");
        if (items.isEmpty())
            return false;
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        boolean in = false;
        for (TimeInterval curr : train.getTimeIntervalList()) {
            if (fi.getFromInterval() == curr)
                in = true;
            if (curr == interval) {
                return in;
            }
            if (fi.getToInterval() == curr) {
                in = false;
                if (i.hasNext())
                    fi = i.next();
                else
                    break;
            }
        }
        return false;
    }

    public Tuple<TimeInterval> getFirstUncoveredPart(Train train, List<TrainsCycleItem> items) {
        if (items == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        if (items.isEmpty()) {
            return new Tuple<TimeInterval>(train.getFirstInterval(), train.getLastInterval());
        }
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        TrainsCycleItem si = fi;
        if (fi.getFromInterval() != train.getFirstInterval()) {
            return new Tuple<TimeInterval>(train.getFirstInterval(), fi.getFromInterval());
        }
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToInterval() != si.getFromInterval()) {
                return new Tuple<TimeInterval>(fi.getToInterval(), si.getFromInterval());
            }
            fi = si;
        }
        if (si.getToInterval() != train.getLastInterval()) {
            return new Tuple<TimeInterval>(si.getToInterval(), train.getLastInterval());
        } else {
            return new Tuple<TimeInterval>(null, null);
        }
    }

    public List<Tuple<TimeInterval>> getAllUncoveredParts(Train train, List<TrainsCycleItem> items) {
        if (items == null) {
            throw new IllegalArgumentException("List cannot be null.");
        }
        if (items.isEmpty()) {
            return Collections.singletonList(new Tuple<TimeInterval>(train.getFirstInterval(), train.getLastInterval()));
        }
        List<Tuple<TimeInterval>> result = new LinkedList<Tuple<TimeInterval>>();
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem fi = i.next();
        TrainsCycleItem si = fi;
        if (fi.getFromInterval() != train.getFirstInterval()) {
            result.add(new Tuple<TimeInterval>(train.getFirstInterval(), fi.getFromInterval()));
        }
        while (i.hasNext()) {
            si = i.next();
            if (fi.getToInterval() != si.getFromInterval()) {
                result.add(new Tuple<TimeInterval>(fi.getToInterval(), si.getFromInterval()));
            }
            fi = si;
        }
        if (si.getToInterval() != train.getLastInterval()) {
            result.add(new Tuple<TimeInterval>(si.getToInterval(), train.getLastInterval()));
        }
        return result;
    }

    public List<List<TimeInterval>> getAllUncoveredLists(Train train, List<TrainsCycleItem> items) {
        List<Tuple<TimeInterval>> tuples = this.getAllUncoveredParts(train, items);
        List<List<TimeInterval>> result = new LinkedList<List<TimeInterval>>();
        for (Tuple<TimeInterval> tuple : tuples) {
            List<TimeInterval> nodes = new LinkedList<TimeInterval>();
            boolean collect = false;
            for (TimeInterval interval : train.getTimeIntervalList()) {
                if (interval.getOwner() instanceof Node) {
                    if (interval == tuple.first) {
                        collect = true;
                    }
                    if (collect) {
                        nodes.add(interval);
                    }
                    if (interval == tuple.second) {
                        collect = false;
                    }
                }
            }
            result.add(nodes);
        }
        return result;
    }

    public List<Pair<TimeInterval, Boolean>> getRouteCoverage(Train train, List<TrainsCycleItem> items) {
        List<Pair<TimeInterval, Boolean>> result = new LinkedList<Pair<TimeInterval, Boolean>>();
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem current = i.hasNext() ? i.next() : null;
        boolean in = false;
        for (TimeInterval interval : train.getTimeIntervalList()) {
            Pair<TimeInterval, Boolean> pair = new Pair<TimeInterval, Boolean>(interval, Boolean.FALSE);
            if (pair.first.isNodeOwner()) {
                if (current != null && pair.first == current.getFromInterval()) {
                    in = true;
                }
                if (in) {
                    pair.second = true;
                }
                if (current != null && pair.first == current.getToInterval()) {
                    in = false;
                    current = i.hasNext() ? i.next() : null;
                    if (current != null && pair.first == current.getFromInterval()) {
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
            if (interval.isNodeOwner()) {
                if (ftci.getFromInterval() == interval) {
                    occupied = true;
                }
                if (newItem.getFromInterval() == interval) {
                    inserted = true;
                }
                if (newItem.getToInterval() == interval) {
                    inserted = false;
                }
                if (ftci.getToInterval() == interval) {
                    occupied = false;
                    ftci = getNext(i, ignoredItem);
                    if (ftci != null && ftci.getFromInterval() == interval) {
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
        if (item.getFromInterval() == item.getToInterval()) {
            return false;
        }
        boolean in = false;
        for (TimeInterval interval : train.getTimeIntervalList()) {
            if (interval.isNodeOwner()) {
                if (!in && (interval == item.getFromInterval())) {
                    in = true;
                }
                if (interval == item.getToInterval()) {
                    return in;
                }
            }
        }
        return false;
    }
}
