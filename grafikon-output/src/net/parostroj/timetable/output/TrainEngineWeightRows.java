package net.parostroj.timetable.output;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.Pair;

/**
 * Creates information about train's allowed weight and engine. 
 * 
 * @author jub
 */
public class TrainEngineWeightRows {

    private Train train;
    private List<TrainEWDataRow> data;

    public TrainEngineWeightRows(Train train) {
        this.train = train;
        this.data = new LinkedList<TrainEWDataRow>();
        this.processData();
    }

    private void processData() {
        // create list of lines ...
        List<Pair<TimeInterval, TrainsCycleItem>> lines = new LinkedList<Pair<TimeInterval, TrainsCycleItem>>();
        for (TimeInterval i : train.getTimeIntervalList()) {
            if (i.isLineOwner()) {
                lines.add(new Pair<TimeInterval, TrainsCycleItem>(i, null));
            }
        }

        if (!this.checkLineClasses(lines) || !this.checkEngineClasses()) {
            this.processOld();
        } else {
            this.processNew(lines);
        }
    }

    private void processOld() {
        String weightStr = (String) train.getAttribute("weight.info");
        if (weightStr != null && weightStr.trim().equals(""))
            weightStr = null;
        for (TrainsCycleItem item : train.getCycles(TrainsCycleType.ENGINE_CYCLE)) {
            String name = TransformUtil.getEngineCycleDescription(item.getCycle());
            data.add(new TrainEWDataRow(train, name, item.getFromInterval().getOwnerAsNode(), item.getToInterval().getOwnerAsNode(), weightStr));
        }
        if (data.size() == 0 && weightStr != null) {
            data.add(new TrainEWDataRow(train, null, null, null, weightStr));
        }
    }

    private void processNew(List<Pair<TimeInterval, TrainsCycleItem>> lines) {
        // engines
        List<TrainsCycleItem> items = train.getCycles(TrainsCycleType.ENGINE_CYCLE);
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem current = i.next();
        boolean in = false;
        for (Pair<TimeInterval, TrainsCycleItem> pair : lines) {
            Train modelTrain = pair.first.getTrain();
            if (current != null && modelTrain.getIntervalBefore(pair.first) == current.getFromInterval()) {
                in = true;
            }
            if (in) {
                pair.second = current;
            }
            if (current != null && modelTrain.getIntervalAfter(pair.first) == current.getToInterval()) {
                in = false;
                current = i.hasNext() ? i.next() : null;
            }
        }

        // generate data
        Iterator<Pair<TimeInterval, TrainsCycleItem>> iter = lines.iterator();
        Pair<TimeInterval, TrainsCycleItem> currentPair;
        TrainsCycleItem lastItem = null;
        TrainsCycleItem lastWrittenItem = null;
        Integer lastWeight = null;
        Node from = null;
        in = false;

        while (iter.hasNext()) {
            currentPair = iter.next();
            TimeInterval currentInterval = currentPair.first;
            Train currentTrain = currentInterval.getTrain();
            TrainsCycleItem currentCycleItem = currentPair.second;
            LineClass currentLineClass = (LineClass) currentInterval.getOwnerAsLine().getAttribute("line.class");
            Integer currentWeight = null;
            if (currentCycleItem != null) {
                currentWeight = ((EngineClass) currentCycleItem.getCycle().getAttribute("engine.class")).getWeightTableRowForSpeed(currentInterval.getSpeed()).getWeight(currentLineClass);
            }
            boolean written = false;
            if (currentCycleItem != null && currentTrain.getIntervalBefore(currentInterval) == currentCycleItem.getFromInterval()) {
                in = true;
                from = currentInterval.getFrom();
            }

            if (!written && in && lastWeight != null && currentWeight.intValue() != lastWeight.intValue()) {
                data.add(new TrainEWDataRow(lastWrittenItem == lastItem ? null : lastItem, from, currentInterval.getFrom(), lastWeight));
                lastWrittenItem = lastItem;
                from = currentInterval.getFrom();
                written = true;
            }

            if (in && currentTrain.getIntervalAfter(currentInterval) == currentCycleItem.getToInterval()) {
                in = false;
                data.add(new TrainEWDataRow(lastWrittenItem == currentCycleItem ? null : currentCycleItem, from, currentInterval.getTo(), currentWeight));
                currentCycleItem = null;
                currentWeight = null;
                lastWrittenItem = null;
                written = true;
            }

            lastItem = currentCycleItem;
            lastWeight = currentWeight;
        }
    }

    private boolean checkLineClasses(List<Pair<TimeInterval, TrainsCycleItem>> lines) {
        for (Pair<TimeInterval, ?> pair : lines) {
            if (pair.first.getOwnerAsLine().getAttribute("line.class") == null) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEngineClasses() {
        if (train.getCycles(TrainsCycleType.ENGINE_CYCLE).size() == 0) {
            return false;
        }
        for (TrainsCycleItem item : train.getCycles(TrainsCycleType.ENGINE_CYCLE)) {
            if (item.getCycle().getAttribute("engine.class") == null) {
                return false;
            }
        }

        return true;
    }

    List<TrainEWDataRow> getData() {
        return Collections.unmodifiableList(data);
    }
}

class TrainEWDataRow {

    private final String engine;
    private final String from;
    private final String to;
    private final String weight;

    public TrainEWDataRow(TrainsCycleItem item, Node from, Node to, Integer weight) {
        this.engine = item != null ? TransformUtil.getEngineCycleDescription(item.getCycle()) : null;
        this.from = from.getName();
        this.to = to.getName();
        this.weight = weight.toString();
    }

    public TrainEWDataRow(Train train, String engine, Node from, Node to, String weight) {
        this.engine = engine;
        this.weight = weight;
        if ((train.getStartNode() == from && train.getEndNode() == to) || from == null || to == null) {
            this.from = null;
            this.to = null;
        } else {
            this.from = from.getName();
            this.to = to.getName();
        }
    }

    public String getEngine() {
        return engine;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getWeight() {
        return weight;
    }
}