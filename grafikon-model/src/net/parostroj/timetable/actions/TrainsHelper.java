package net.parostroj.timetable.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.Pair;

/**
 * Helper actions for trains.
 * 
 * @author jub
 */
public class TrainsHelper {

    public static final Integer getWeight(TimeInterval interval) {
        Pair<Integer, TrainsCycleItem> weightAndCycle = getWeightAndCycle(interval);
        if (weightAndCycle == null || weightAndCycle.first == null)
            return null;
        else
            return weightAndCycle.first;
    }

    public static final Pair<Integer, TrainsCycleItem> getWeightAndCycle(TimeInterval interval) {
        if (!interval.isLineOwner()) {
            throw new IllegalArgumentException("Weight can be returned only for line interval.");
        }
        Pair<Integer, TrainsCycleItem> retValue = null;
        LineClass lineClass = (LineClass) interval.getOwnerAsLine().getAttribute("line.class");
        Pair<EngineClass, TrainsCycleItem> engineClass = getEngineClassAndCycle(interval);
        if (lineClass != null && engineClass != null && engineClass.first != null) {
            retValue = new Pair<Integer, TrainsCycleItem>(engineClass.first.getWeightTableRowForSpeed(interval.getSpeed()).getWeight(lineClass), engineClass.second);
        }
        return retValue;
    }

    public static final Integer convertWeightToLengt(Train train, TrainDiagram diagram, Integer weight) {
        Double ratio = Boolean.TRUE.equals(train.getAttribute("empty")) ?
            (Double)diagram.getAttribute("weight.ratio.empty") :
            (Double)diagram.getAttribute("weight.ratio.loaded");
        if (ratio == null || weight == null)
            return null;
        return (int)(weight * ratio);
    }

    public static final Pair<EngineClass, TrainsCycleItem> getEngineClassAndCycle(TimeInterval interval) {
        Train train = interval.getTrain();
        TrainsCycleItem item = train.getCycleItemForInterval(TrainsCycleType.ENGINE_CYCLE, interval);
        if (item != null) {
            return new Pair<EngineClass, TrainsCycleItem>((EngineClass) item.getCycle().getAttribute("engine.class"), item);
        }
        return null;
    }

    public static final EngineClass getEngineClass(TimeInterval interval) {
        Pair<EngineClass, TrainsCycleItem> pair = getEngineClassAndCycle(interval);
        if (pair != null)
            return pair.first;
        else
            return null;
    }

    public static final List<Pair<TimeInterval, Pair<Integer, TrainsCycleItem>>> getWeightList(Train train) {
        // if the train is not coverred return null
        if (!train.isCovered(TrainsCycleType.ENGINE_CYCLE))
            return null;
        List<Pair<TimeInterval, Pair<Integer, TrainsCycleItem>>> result = new ArrayList<Pair<TimeInterval, Pair<Integer, TrainsCycleItem>>>();
        for (TimeInterval interval : train.getTimeIntervalList()) {
            if (interval.isNodeOwner())
                result.add(new Pair<TimeInterval, Pair<Integer, TrainsCycleItem>>(interval, null));
            else {
                Pair<Integer, TrainsCycleItem> weight = getWeightAndCycle(interval);
                if (weight == null)
                    return null;
                else {
                    result.add(new Pair<TimeInterval, Pair<Integer, TrainsCycleItem>>(interval, weight));
                }
            }
        }
        
        return result;
    }

    public static final Integer getNextWeight(Node node, Train train) {
        List<Pair<TimeInterval, Pair<Integer, TrainsCycleItem>>> weightList = getWeightList(train);
        Integer retValue = null;
        if (weightList != null) {
            Iterator<Pair<TimeInterval, Pair<Integer, TrainsCycleItem>>> i = weightList.iterator();
            // skip to node
            while (i.hasNext()) {
                Pair<TimeInterval, Pair<Integer, TrainsCycleItem>> pairI = i.next();
                // node found
                if (pairI.first.getOwner() == node)
                    break;
            }
            // check weight
            while (i.hasNext()) {
                Pair<TimeInterval, Pair<Integer, TrainsCycleItem>> pairI = i.next();
                if (pairI.first.isLineOwner()) {
                    if (retValue == null || retValue > pairI.second.first)
                        retValue = pairI.second.first;
                } else if (pairI.first.isNodeOwner()) {
                    // next stop found
                    if (pairI.first.getType().isStop())
                        break;
                }
            }
        }
        return retValue;
    }
}
