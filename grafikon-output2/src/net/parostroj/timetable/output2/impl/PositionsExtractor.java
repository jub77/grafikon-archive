package net.parostroj.timetable.output2.impl;

import java.util.LinkedList;
import java.util.List;
import net.parostroj.timetable.actions.TrainsCycleSort;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.TransformUtil;

/**
 * Extracts list of positions from train diagram.
 *
 * @author jub
 */
public class PositionsExtractor {

    private TrainDiagram diagram;

    public PositionsExtractor(TrainDiagram diagram) {
        this.diagram = diagram;
    }

    public List<Position> getStartPositionsEngines() {
        List<Position> result = new LinkedList<Position>();
        for (TrainsCycle ecCycle : this.sortTrainsCycleList(diagram.getCycles(TrainsCycleType.ENGINE_CYCLE))) {
            if (!ecCycle.isEmpty()) {
                TrainsCycleItem start = ecCycle.iterator().next();
                String startName = start.getFromInterval().getOwnerAsNode().getName();
                result.add(new Position(ecCycle.getName(), TransformUtil.getEngineCycleDescription(ecCycle), startName, start.getTrain().getName()));
            }
        }
        return result;
    }

    public List<Position> getStartPositionsTrainUnits() {
        List<Position> result = new LinkedList<Position>();
        for (TrainsCycle tucCycle : this.sortTrainsCycleList(diagram.getCycles(TrainsCycleType.TRAIN_UNIT_CYCLE))) {
            if (!tucCycle.isEmpty()) {
                TrainsCycleItem start = tucCycle.iterator().next();
                String startName = start.getFromInterval().getOwnerAsNode().getName();
                result.add(new Position(tucCycle.getName(), tucCycle.getDescription(), startName, start.getTrain().getName()));
            }
        }
        return result;
    }

    public List<Position> getEndPositionsEngines() {
        return null;
    }

    public List<Position> getEndPositionsTrainUnits() {
        return null;
    }

    private List<TrainsCycle> sortTrainsCycleList(List<TrainsCycle> list) {
        TrainsCycleSort sort = new TrainsCycleSort(TrainsCycleSort.Type.ASC);
        return sort.sort(list);
    }
}
