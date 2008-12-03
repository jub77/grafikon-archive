package net.parostroj.timetable.actions;

import net.parostroj.timetable.model.*;

/**
 * Helper actions for trains.
 * 
 * @author jub
 */
public class TrainsHelper {

    public static final Integer getWeight(TimeInterval interval) {
        if (!interval.isLineOwner()) {
            throw new IllegalArgumentException("Weight can be returned only for line interval.");
        }
        Integer retValue = null;
        LineClass lineClass = (LineClass) interval.getOwnerAsLine().getAttribute("line.class");
        EngineClass engineClass = getEngineClass(interval);
        if (lineClass != null && engineClass != null) {
            return engineClass.getWeightTableRowForSpeed(interval.getSpeed()).getWeight(lineClass);
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

    public static final EngineClass getEngineClass(TimeInterval interval) {
        Train train = interval.getTrain();
        TrainsCycleItem item = train.getCycleItemForInterval(TrainsCycleType.ENGINE_CYCLE, interval);
        if (item != null) {
            return (EngineClass) item.getCycle().getAttribute("engine.class");
        }
        return null;
    }
}
