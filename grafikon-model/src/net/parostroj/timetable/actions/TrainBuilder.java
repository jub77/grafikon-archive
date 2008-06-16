package net.parostroj.timetable.actions;

import java.util.UUID;
import net.parostroj.timetable.model.*;

/**
 * TrainCreator creates train with specified parameters for established route.
 *
 * @author jub
 */
public class TrainBuilder {

    /**
     * creates instance..
     */
    public TrainBuilder() {
    }

    /**
     * creates new train with the same data and same route.
     * 
     * @param id id
     * @param number number of the name train
     * @param time new start time
     * @param copiedTrain original train
     * @return created train
     */
    public Train createTrain(String id, String number, int time, Train copiedTrain) {
        // create new train with the same data
        Train train = new Train(id, number, copiedTrain.getType());
        train.setDescription(copiedTrain.getDescription());
        train.setTopSpeed(copiedTrain.getTopSpeed());
        train.setAttributes(new Attributes(copiedTrain.getAttributes()));

        // create copy of time intervals
        for (TimeInterval copiedInterval : copiedTrain.getTimeIntervalList()) {
            TimeInterval interval = new TimeInterval(UUID.randomUUID().toString(), copiedInterval);
            // redirect to a new train
            interval.setTrain(train);

            // add interval
            train.addInterval(interval);
        }

        // move to new time (automatically attaches the train to nodes and lines)
        train.move(time);

        return train;
    }

    /**
     * creates train of specified type starting on specified time.
     *
     * @param id id
     * @param name name
     * @param trainType train type
     * @param topSpeed top speed
     * @param route route
     * @param time starting time
     * @param data data
     * @param defaultStop default stop time
     * @return created train
     */
    public Train createTrain(String id, String name, TrainType trainType, int topSpeed, Route route, int time, TrainDiagram diagram, int defaultStop) {
        Train train = new Train(id, name, trainType);

        train.setTopSpeed(topSpeed);

        int i = 0;
        int last = route.getSegments().size() - 1;

        // create new data
        Node lastNode = null;
        int nextStop = 1;
        int lastStop = 0;
        for (RouteSegment part : route.getSegments()) {
            if (part.asLine() != null) {
                int index = route.getSegments().indexOf(part);
                Node nextNode = (Node) route.getSegments().get(index + 1);
                lastStop = nextStop;
                if ((index + 1) == last) {
                    nextStop = 1;
                } else if (nextNode.getType() == NodeType.SIGNAL) {
                    nextStop = 0;
                } else {
                    nextStop = defaultStop;
                }
            }

            TimeIntervalType type = null;
            if (part.asNode() != null) {
                if (i == 0) {
                    type = TimeIntervalType.NODE_START;
                } else if (i == last) {
                    type = TimeIntervalType.NODE_END;
                } else {
                    if (nextStop == 0) {
                        type = TimeIntervalType.NODE_THROUGH;
                    } else {
                        type = TimeIntervalType.NODE_STOP;
                    }
                }
            } else if (part.asLine() != null) {
                if (lastStop == 0 && nextStop == 0) {
                    type = TimeIntervalType.LINE_THROUGH;
                } else if (lastStop != 0 && nextStop != 0) {
                    type = TimeIntervalType.LINE_START_STOP;
                } else if (lastStop == 0) {
                    type = TimeIntervalType.LINE_THROUGH_STOP;
                } else {
                    type = TimeIntervalType.LINE_START_THROUGH;
                }
            } else {
                type = TimeIntervalType.NODE_THROUGH;
            }
            TimeInterval interval = null;
            if (part.asNode() != null) {
                interval = part.asNode().createTimeInterval(UUID.randomUUID().toString(), train, time, diagram, type, nextStop);
                lastNode = part.asNode();
            } else {
                TimeIntervalDirection direction = (part.asLine().getFrom() == lastNode) ? TimeIntervalDirection.FORWARD : TimeIntervalDirection.BACKWARD;
                interval = part.asLine().createTimeInterval(UUID.randomUUID().toString(), train, time, diagram, type, direction, TimeInterval.NO_SPEED);
            }
            time = interval.getEnd();
            train.addInterval(interval);
            i++;
        }

        return train;
    }
}
