package net.parostroj.timetable.actions;

import java.util.LinkedList;
import java.util.List;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.utils.Pair;

public class TrainTestResult {

    private Train train;

    private List<Pair<Object, TimeInterval>> list = new LinkedList<Pair<Object, TimeInterval>>();

    public TrainTestResult(Train train) {
        this.train = train;
    }

    public List<Pair<Object, TimeInterval>> getList() {
        return list;
    }

    public void add(Object part, TimeInterval interval) {
        list.add(new Pair<Object, TimeInterval>(part, interval));
    }

    public Train getTrain() {
        return train;
    }
}
