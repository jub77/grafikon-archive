package net.parostroj.timetable.actions;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.logging.Logger;
import net.parostroj.timetable.model.*;

/**
 * Builder for creating trains.
 * 
 * @author jub
 */
public class TrainIntervalsBuilder {

    private static final Logger LOG = Logger.getLogger(TrainIntervalsBuilder.class.getName());
    private TrainDiagram diagram;
    private Train train;
    private TimeInterval lastInterval;
    private int startTime;
    private boolean finished;
    private List<TimeInterval> timeIntervals;

    public TrainIntervalsBuilder(TrainDiagram diagram, Train train, int startTime) {
        this.diagram = diagram;
        this.train = train;
        this.lastInterval = null;
        this.startTime = startTime;
        this.finished = false;
        this.timeIntervals = new LinkedList<TimeInterval>();
    }

    public void addNode(String intervalId, Node node, NodeTrack track, int stop, Attributes attributes) {
        if (intervalId == null) {
            LOG.warning("Adding interval with not specified id (fix - generated): " + node);
            intervalId = UUID.randomUUID().toString();
        }
        if (finished) {
            throw new IllegalStateException("Cannot add node time interval to finished train.");
        }
        if (lastInterval == null) {
            // create first time interval
            lastInterval = new TimeInterval(intervalId, train, node, startTime, startTime, TimeIntervalType.NODE_START, track);
        } else {
            if (lastInterval.getOwner().asLine() == null) {
                throw new IllegalStateException("Last interval owner was not line.");
            }
            lastInterval = new TimeInterval(intervalId,
                    train, node, 0, stop, stop == 0 ? TimeIntervalType.NODE_THROUGH : TimeIntervalType.NODE_STOP, track);
        }
        lastInterval.setAttributes(attributes);
        timeIntervals.add(lastInterval);
    }

    public void addLine(String intervalId, Line line, LineTrack track, int speed, Attributes attributes) {
        if (intervalId == null) {
            LOG.warning("Adding interval with not specified id (fix - generated): " + line);
            intervalId = UUID.randomUUID().toString();
        }
        if (finished) {
            throw new IllegalStateException("Cannot add line time interval to finished train.");
        }
        if (lastInterval == null || lastInterval.getOwner().asNode() == null) {
            throw new IllegalStateException("Last interval owner was not node.");
        }

        lastInterval = new TimeInterval(
                intervalId, train, line, 0, 0, speed,
                lastInterval.getOwner().asNode() == line.getFrom() ? TimeIntervalDirection.FORWARD : TimeIntervalDirection.BACKWARD,
                lastInterval.getType() == TimeIntervalType.NODE_THROUGH ? TimeIntervalType.LINE_THROUGH_STOP : TimeIntervalType.LINE_START_STOP,
                track);
        lastInterval.setAttributes(attributes);
        timeIntervals.add(lastInterval);
    }

    public void finish() {
        if (finished) {
            throw new IllegalStateException("Cannot finish already finished train.");
        }
        // finish last node interval
        if (lastInterval == null || lastInterval.getOwner().asNode() == null || lastInterval.getType() == TimeIntervalType.NODE_START) {
            throw new IllegalStateException("Cannot finish train interval building.");
        }
        
        ListIterator<TimeInterval> iterator = timeIntervals.listIterator();
        TimeInterval last = iterator.next();
        int start = startTime;
        while (iterator.hasNext()) {
            TimeInterval current = iterator.next();
            TimeInterval created = null;
            if (current.getOwner().asNode() != null) {
                // finish last line interval
                TimeIntervalType type = null;
                if (current.getLength() != 0 || !iterator.hasNext())
                    type = last.getType().changeToNextStop();
                else
                    type = last.getType().changeToNextThrough();
                created = last.getOwner().asLine().createTimeInterval(last.getId(), train, start, diagram, type, last.getDirection(), last.getSpeed());
                created.setTrack(last.getTrack());
            } else {
                // finish last node interval
                created = last.getOwner().asNode().createTimeInterval(last.getId(), train, start, diagram, last.getType(), last.getLength());
                created.setTrack(last.getTrack());
            }
            created.setAttributes(last.getAttributes());
            train.addInterval(created);
            start = created.getEnd();
            last = current;
        }
        // finish last node
        TimeInterval lastCreated = last.getOwner().asNode().createTimeInterval(last.getId(), train, start, diagram, TimeIntervalType.NODE_END, last.getLength());
        lastCreated.setAttributes(last.getAttributes());
        lastCreated.setTrack(last.getTrack());
        train.addInterval(lastCreated);
        
        finished = true;
    }
}
