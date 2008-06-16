package net.parostroj.timetable.model;

import java.util.*;
import net.parostroj.timetable.actions.TrainsCycleHelper;
import net.parostroj.timetable.model.events.TrainEvent;
import net.parostroj.timetable.model.events.TrainListener;
import net.parostroj.timetable.utils.Pair;
import net.parostroj.timetable.utils.Tuple;

/**
 * Train.
 *
 * @author jub
 */
public class Train implements AttributesHolder, ObjectWithId {

    /** ID. */
    private final String id;
    /** Train number. */
    private String number;
    /** Description. */
    private String description;
    /** Train type. */
    private TrainType type;
    /** List of time intervals. */
    private TimeIntervalList timeIntervalList;
    /** Top speed (comment - 0 .. no speed defined (speed should be determined by train type)). */
    private int topSpeed = NO_TOP_SPEED;
    /** No top speed constant. */
    public static final int NO_TOP_SPEED = 0;
    /** Cycles. */
    private Map<TrainsCycleType, List<TrainsCycleItem>> cycles;
    /* Attributes of the train. */
    private Attributes attributes;
    /* cached data */
    private String _cachedName;
    private String _cachedCompleteName;
    private GTListenerSupport<TrainListener, TrainEvent> listenerSupport;

    /**
     * Constructor.
     * 
     * @param id id
     */
    public Train(String id) {
        this.id = id;
        timeIntervalList = new TimeIntervalList();
        attributes = new Attributes();
        cycles = new EnumMap<TrainsCycleType, List<TrainsCycleItem>>(TrainsCycleType.class);
        listenerSupport = new GTListenerSupport<TrainListener, TrainEvent>(new GTEventSender<TrainListener, TrainEvent>() {

            @Override
            public void fireEvent(TrainListener listener, TrainEvent event) {
                listener.trainChanged(event);
            }
        });
    }

    /**
     * creates instance with name and type.
     *
     * @param id  id
     * @param number train number
     * @param trainType train type
     */
    public Train(String id, String number, TrainType trainType) {
        this(id);
        this.number = number;
        this.type = trainType;
    }

    /**
     * @return id of the train
     */
    @Override
    public String getId() {
        return id;
    }

    public void addListener(TrainListener listener) {
        listenerSupport.addListener(listener);
    }

    public void removeListener(TrainListener listener) {
        listenerSupport.removeListener(listener);
    }

    /**
     * @return number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number number to be set
     */
    public void setNumber(String number) {
        this.clearCachedData();
        this.number = number;
        this.listenerSupport.fireEvent(new TrainEvent(this, "number"));
    }

    /**
     * @return name of the train depending on the pattern
     */
    public String getName() {
        if (_cachedName == null) {
            _cachedName = type.formatTrainName(this);
        }
        return _cachedName;
    }

    /**
     * @return complete name of the train depending on the pattern
     */
    public String getCompleteName() {
        if (_cachedCompleteName == null) {
            _cachedCompleteName = type.formatTrainCompleteName(this);
        }
        return _cachedCompleteName;
    }

    /**
     * clears cached train names.
     */
    public void clearCachedData() {
        _cachedCompleteName = null;
        _cachedName = null;
    }

    /**
     * @return description of the train
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description sets description
     */
    public void setDescription(String description) {
        this.clearCachedData();
        this.description = description;
        this.listenerSupport.fireEvent(new TrainEvent(this, "description"));
    }

    /**
     * @return top speed
     */
    public int getTopSpeed() {
        return topSpeed;
    }

    /**
     * @param topSpeed top speed to be set
     */
    public void setTopSpeed(int topSpeed) {
        this.topSpeed = topSpeed;
        this.listenerSupport.fireEvent(new TrainEvent(this, "topSpeed"));
    }

    @Override
    public String toString() {
        return number + "(" + ((type != null) ? type.getDesc() : "<none>") + "," + topSpeed + ")";
    }

    /**
     * @return the type
     */
    public TrainType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TrainType type) {
        this.clearCachedData();
        this.type = type;
        this.listenerSupport.fireEvent(new TrainEvent(this, "type"));
    }

    /**
     * @return the intervals
     */
    public List<TimeInterval> getTimeIntervalList() {
        return Collections.unmodifiableList(timeIntervalList);
    }

    public Map<TrainsCycleType, List<TrainsCycleItem>> getCyclesMap() {
        EnumMap<TrainsCycleType, List<TrainsCycleItem>> modMap = new EnumMap<TrainsCycleType, List<TrainsCycleItem>>(TrainsCycleType.class);
        for (Map.Entry<TrainsCycleType, List<TrainsCycleItem>> entry : cycles.entrySet()) {
            modMap.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(cycles);
    }

    public List<TrainsCycleItem> getCycles(TrainsCycleType type) {
        return Collections.unmodifiableList(this.getCyclesIntern(type));
    }

    private List<TrainsCycleItem> getCyclesIntern(TrainsCycleType type) {
        if (!cycles.containsKey(type)) {
            cycles.put(type, new LinkedList<TrainsCycleItem>());
        }
        return cycles.get(type);
    }

    protected void addCycleItem(TrainsCycleItem item) {
        TrainsCycleType cycleType = item.getCycle().getType();
        TrainsCycleHelper.getHelper(cycleType).addCycleItem(this, this.getCyclesIntern(cycleType), item);
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.CYCLE_ITEM));
    }

    protected void removeCycleItem(TrainsCycleItem item) {
        TrainsCycleType cycleType = item.getCycle().getType();
        this.getCyclesIntern(cycleType).remove(item);
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.CYCLE_ITEM));
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.clearCachedData();
        this.attributes = attributes;
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public Object removeAttribute(String key) {
        Object o = attributes.remove(key);
        this.listenerSupport.fireEvent(new TrainEvent(this, key));
        return o;
    }

    @Override
    public void setAttribute(String key, Object value) {
        this.clearCachedData();
        attributes.put(key, value);
        this.listenerSupport.fireEvent(new TrainEvent(this, key));
    }

    // methods for testing/attaching/detaching trains in/to/from net
    /**
     * @return <code>true</code> if there are overlapping intervals for the train
     */
    public boolean isConflicting() {
        for (TimeInterval interval : timeIntervalList) {
            if (interval.getOverlappingIntervals() != null && interval.getOverlappingIntervals().size() != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return set of train with conflicts with this train
     */
    public Set<Train> getConflictingTrains() {
        Set<Train> conflictingTrains = null;
        for (TimeInterval interval : timeIntervalList) {
            if (interval.getOverlappingIntervals() != null && interval.getOverlappingIntervals().size() != 0) {
                for (TimeInterval i2 : interval.getOverlappingIntervals()) {
                    if (conflictingTrains == null) {
                        conflictingTrains = new HashSet<Train>();
                    }
                    conflictingTrains.add(i2.getTrain());
                }
            }
        }
        if (conflictingTrains == null) {
            return Collections.emptySet();
        } else {
            return conflictingTrains;
        }
    }

    /**
     * @return <code>true</code> if all stops that needs platform have one
     */
    public boolean checkPlatforms() {
        for (TimeInterval interval : timeIntervalList) {
            if (!interval.isPlatformOk()) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns first node of the train timetable.
     * 
     * @return first node
     */
    public Node getStartNode() {
        return timeIntervalList.get(0).getOwner().asNode();
    }

    /**
     * returns last node of the train.
     * 
     * @return last node
     */
    public Node getEndNode() {
        return timeIntervalList.get(timeIntervalList.size() - 1).getOwner().asNode();
    }

    /**
     * returns start time of the train.
     * 
     * @return start time
     */
    public int getStartTime() {
        return timeIntervalList.get(0).getStart();
    }

    /**
     * returns end time of the train.
     * 
     * @return end time
     */
    public int getEndTime() {
        return timeIntervalList.get(timeIntervalList.size() - 1).getEnd();
    }

    /**
     * shifts train with specified amount of time. The value can be
     * positive for shifting forwards or negative for 
     * 
     * @param timeShift time
     */
    public void shift(int timeShift) {
        timeIntervalList.shift(timeShift);
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.TIME_INTERVAL_LIST));
    }

    /**
     * moves train to the new starting time.
     * 
     * @param time starting time
     */
    public void move(int time) {
        timeIntervalList.move(time);
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.TIME_INTERVAL_LIST));
    }

    /**
     * changes time for stop for specified node.
     * 
     * @param nodeInterval node interval
     * @param length length of the stop
     * @param info model info
     */
    public void changeStopTime(TimeInterval nodeInterval, int length, TrainDiagram diagram) {
        // check time
        if (length < 0) {
            throw new IllegalArgumentException("Stop time cannot be negative.");        // change stop time and move others
        }
        int nextStart = timeIntervalList.get(0).getStart();
        boolean changed = false;
        for (TimeInterval interval : timeIntervalList) {
            if ((timeIntervalList.getIntervalAfter(interval) == nodeInterval) && (length == 0) && (interval.getType().isNextStop())) {
                // change type of interval
                interval.setLength(((Line) interval.getOwner()).computeRunningTime(this, interval.getSpeed(), diagram, interval.getType().changeToNextThrough()).first);
                interval.setType(interval.getType().changeToNextThrough());
                changed = true;
            }
            if ((timeIntervalList.getIntervalAfter(interval) == nodeInterval) && (length != 0) && (!interval.getType().isNextStop())) {
                // change type of interval
                interval.setLength(((Line) interval.getOwner()).computeRunningTime(this, interval.getSpeed(), diagram, interval.getType().changeToNextStop()).first);
                interval.setType(interval.getType().changeToNextStop());
                changed = true;
            }
            if ((timeIntervalList.getIntervalBefore(interval) == nodeInterval) && (length == 0) && (interval.getType().isPreviousStop())) {
                // change type of interval
                interval.setLength(((Line) interval.getOwner()).computeRunningTime(this, interval.getSpeed(), diagram, interval.getType().changeToPreviousThrough()).first);
                interval.setType(interval.getType().changeToPreviousThrough());
                changed = true;
            }
            if ((timeIntervalList.getIntervalBefore(interval) == nodeInterval) && (length != 0) && (!interval.getType().isPreviousStop())) {
                // change type of interval
                interval.setLength(((Line) interval.getOwner()).computeRunningTime(this, interval.getSpeed(), diagram, interval.getType().changeToPreviousStop()).first);
                interval.setType(interval.getType().changeToPreviousStop());
                changed = true;
            }

            if (interval == nodeInterval) {
                if (length == 0) {
                    interval.setType(TimeIntervalType.NODE_THROUGH);
                } else {
                    interval.setType(TimeIntervalType.NODE_STOP);
                }
                interval.setLength(length);
                changed = true;
            }
            if (changed || interval.getStart() != nextStart) {
                interval.removeFromOwner();
                interval.move(nextStart);
                interval.addToOwner();
            }

            changed = false;
            nextStart = interval.getEnd();
        }
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.TIME_INTERVAL_LIST));
    }

    /**
     * changes velocity of the train on the specified line.
     * 
     * @param lineInterval line interval
     * @param velocity velocity to be set
     * @param modelInfo model info
     */
    public void changeVelocity(TimeInterval lineInterval, int velocity, TrainDiagram diagram) {
        // change velocity
        boolean moveNext = false;
        int shift = 0;
        for (TimeInterval interval : timeIntervalList) {
            if (moveNext) {
                interval.removeFromOwner();
                interval.shift(shift);
                interval.addToOwner();
            }

            if (lineInterval == interval) {
                // compute new speed
                Pair<Integer, Integer> c = lineInterval.getOwnerAsLine().computeRunningTime(this, velocity, diagram, interval.getType());
                if (c.second == interval.getSpeed()) // do nothing
                {
                    break;
                }
                shift = c.first - interval.getLength();
                interval.removeFromOwner();
                interval.setLength(c.first);
                interval.setSpeed(c.second);
                interval.addToOwner();
                moveNext = true;
            }
        }
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.TIME_INTERVAL_LIST));
    }

    /**
     * changes node track.
     * 
     * @param nodeInterval node interval
     * @param nodeTrack node track to be changed
     */
    public void changeNodeTrack(TimeInterval nodeInterval, NodeTrack nodeTrack) {
        if (!nodeInterval.isNodeOwner())
            throw new IllegalArgumentException("No node interval.");
        if (nodeInterval != null) {
            nodeInterval.removeFromOwner();
            nodeInterval.setTrack(nodeTrack);
            nodeInterval.addToOwner();
        }
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.TIME_INTERVAL_LIST));
    }

    /**
     * changes line track.
     * 
     * @param lineInterval line interval
     * @param lineTrack line track to be changed
     */
    public void changeLineTrack(TimeInterval lineInterval, LineTrack lineTrack) {
        if (!lineInterval.isLineOwner())
            throw new IllegalArgumentException("No line interval.");
        if (lineInterval != null) {
            lineInterval.removeFromOwner();
            lineInterval.setTrack(lineTrack);
            lineInterval.addToOwner();
        }
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.TIME_INTERVAL_LIST));
    }

    /**
     * Recalculates all line intervals.
     * 
     * @param info model info
     */
    public void recalculate(TrainDiagram diagram) {
        TimeInterval s = timeIntervalList.get(1);
        if (s.getType() == TimeIntervalType.LINE_THROUGH) {
            s.setType(TimeIntervalType.LINE_START_THROUGH);
        }
        s = timeIntervalList.get(timeIntervalList.size() - 2);
        if (s.getType() == TimeIntervalType.LINE_THROUGH) {
            s.setType(TimeIntervalType.LINE_THROUGH_STOP);
        }
        if (s.getType() == TimeIntervalType.LINE_START_THROUGH) {
            s.setType(TimeIntervalType.LINE_START_STOP);
        }
        // recalculate whole train
        int nextStart = timeIntervalList.get(0).getStart();
        boolean changed = false;
        for (TimeInterval interval : timeIntervalList) {
            if (interval.getOwner() instanceof Line) {
                Line line = (Line) interval.getOwner();
                // compute new speed
                Pair<Integer, Integer> c = line.computeRunningTime(this, interval.getSpeed(), diagram, interval.getType());
                interval.setLength(c.first);
                interval.setSpeed(c.second);
                changed = true;
            }
            if (changed || nextStart != interval.getStart()) {
                interval.removeFromOwner();
                interval.move(nextStart);
                interval.addToOwner();
                changed = false;
            }
            nextStart = interval.getEnd();
        }
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.TIME_INTERVAL_LIST));
    }

    /**
     * adds interval to the train.
     * 
     * @param interval interval
     */
    public void addInterval(TimeInterval interval) {
        timeIntervalList.addIntervalLastForTrain(interval);
        this.listenerSupport.fireEvent(new TrainEvent(this, TrainEvent.Type.TIME_INTERVAL_LIST));
    }

    /**
     * @param interval interval
     * @return interval directly before given interval
     */
    public TimeInterval getIntervalBefore(TimeInterval interval) {
        return timeIntervalList.getIntervalBefore(interval);
    }

    /**
     * @param interval interval
     * @return interval directly after given interval
     */
    public TimeInterval getIntervalAfter(TimeInterval interval) {
        return timeIntervalList.getIntervalAfter(interval);
    }

    /**
     * checks if all lines have given attribute.
     * 
     * @param key key
     * @param value value
     * @return result of the check
     */
    public boolean allLinesHaveAttribute(String key, Object value) {
        for (TimeInterval interval : timeIntervalList) {
            if (interval.getOwner() instanceof Line) {
                if (!value.equals(((Line) interval.getOwner()).getAttribute(key))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks if all nodes have given attribute.
     * 
     * @param key key
     * @param value value
     * @return result of the check
     */
    public boolean allNodesHaveAttribute(String key, Object value) {
        for (TimeInterval interval : timeIntervalList) {
            if (interval.getOwner() instanceof Node) {
                if (!value.equals(((Node) interval.getOwner()).getAttribute(key))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks if at least one line has given attribute.
     * 
     * @param key key
     * @param value value
     * @return result of the check
     */
    public boolean oneLineHasAttribute(String key, Object value) {
        for (TimeInterval interval : timeIntervalList) {
            if (interval.getOwner() instanceof Line) {
                if (value.equals(((Line) interval.getOwner()).getAttribute(key))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks if at least one node has given attribute.
     * 
     * @param key key
     * @param value value
     * @return result of the check
     */
    public boolean oneNodeHasAttribute(String key, Object value) {
        for (TimeInterval interval : timeIntervalList) {
            if (interval.getOwner() instanceof Node) {
                if (value.equals(((Node) interval.getOwner()).getAttribute(key))) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void attach() {
        for (TimeInterval interval : timeIntervalList) {
            interval.addToOwner();
        }
    }

    protected void detach() {
        for (TimeInterval interval : timeIntervalList) {
            interval.removeFromOwner();
        }
    }
    
    /**
     * fires given train event for this train (Useful for TrainsCycleItem and
     * TimeIntervalList).
     * 
     * @param event train event
     */
    protected void fireTrainEvent(TrainEvent event) {
        listenerSupport.fireEvent(event);
    }
    
    /**
     * returns if the train is covered by this type of the trains cycle.
     * 
     * @param type trains cycle type
     * @return covered
     */
    public boolean isCovered(TrainsCycleType type) {
        return TrainsCycleHelper.getHelper(type).isTrainCovered(this, this.getCyclesIntern(type));
    }
    
    /**
     * returns pair of nodes that indicates first uncovered interval.
     * 
     * @param type trains cycle type
     * @return interval
     */
    public Tuple<TimeInterval> getFirstUncoveredPart(TrainsCycleType type) {
        return TrainsCycleHelper.getHelper(type).getFirstUncoveredPart(this, this.getCyclesIntern(type));
    }
    
    /**
     * returns all uncovered parts.
     * 
     * @param type trains cycle type
     * @return list of intervals
     */
    public List<Tuple<TimeInterval>> getAllUncoveredParts(TrainsCycleType type) {
        return TrainsCycleHelper.getHelper(type).getAllUncoveredParts(this, this.getCyclesIntern(type));
    }
    
    /**
     * returns all uncovered parts (as list of nodes).
     * 
     * @param type trains cycle type
     * @return list of intervals (as list of nodes)
     */
    public List<List<TimeInterval>> getAllUncoveredLists(TrainsCycleType type) {
        return TrainsCycleHelper.getHelper(type).getAllUncoveredLists(this, this.getCyclesIntern(type));
    }
    
    public List<Pair<TimeInterval, Boolean>> getRouteCoverage(TrainsCycleType type) {
        return TrainsCycleHelper.getHelper(type).getRouteCoverage(this, this.getCyclesIntern(type));
    }
    
    /**
     * returns list of nodes from from noded to to node.
     * 
     * @param from from node
     * @param to to node
     * @return list of nodes in route
     */
    public List<Node> convertToNodeList(Node from, Node to) {
        List<Node> nodes = new LinkedList<Node>();
        boolean collect = false;
        for (TimeInterval interval : getTimeIntervalList()) {
            if (interval.getOwner() instanceof Node) {
                Node node = interval.getOwner().asNode();
                if (from == node)
                    collect = true;
                if (collect)
                    nodes.add(node);
                if (to == node)
                    collect = false;
            }
        }
        return nodes;
    }
    
    public boolean testAddCycle(TrainsCycleItem newItem, TrainsCycleItem ignoredItem) {
        return TrainsCycleHelper.getHelper(newItem.getCycle().getType()).testAddCycle(this, getCyclesIntern(newItem.getCycle().getType()), newItem, ignoredItem);
    }
    
    public TimeInterval getFirstInterval() {
        return timeIntervalList.get(0);
    }
    
    public TimeInterval getLastInterval() {
        return timeIntervalList.get(timeIntervalList.size() - 1);
    }
    
    public TimeInterval getIntervalById(String id) {
        for (TimeInterval interval : timeIntervalList) {
            if (interval.getId().equals(id))
                return interval;
        }
        return null;
    }
}
