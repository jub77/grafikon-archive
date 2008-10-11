package net.parostroj.timetable.gui.views;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.utils.Pair;

/**
 * Class for collecting regions of the trains from graphical timetable.
 * 
 * @author jub
 */
public class TrainRegionCollector {
    
    private Map<Train,List<Pair<Shape, TimeInterval>>> regions;
    
    private boolean collected;
    
    private Train modifiedTrain;
    
    // sensitivity radius
    private int radius;

    public TrainRegionCollector(int radius) {
        regions = new HashMap<Train, List<Pair<Shape, TimeInterval>>>();
        collected = false;
        modifiedTrain = null;
        this.radius = radius;
    }
    
    public void addRegion(TimeInterval interval,Shape shape) {
        // check if the regions are already collected
        if (collected && modifiedTrain != interval.getTrain())
            return;
        // create key if doesn't exist
        if (!regions.containsKey(interval.getTrain())) {
            regions.put(interval.getTrain(), new LinkedList<Pair<Shape, TimeInterval>>());
        }
        // add shape
        regions.get(interval.getTrain()).add(new Pair<Shape, TimeInterval>(shape, interval));
    }

    public void finishCollecting() {
        this.collected = true;
        modifiedTrain = null;
    }
    
    public boolean isCollecting(Train train) {
        return (!collected || modifiedTrain == train);
    }
    
    public void deleteTrain(Train train) {
        regions.remove(train);
    }
    
    public void newTrain(Train train) {
        modifiedTrain = train;
    }
    
    public void modifiedTrain(Train train) {
        regions.remove(train);
        modifiedTrain = train;
    }
    
    public List<TimeInterval> getTrainForPoint(int x, int y) {
        Rectangle2D cursor = new Rectangle2D.Double(x - radius, y - radius, radius * 2, radius * 2);
        List<TimeInterval> list = new LinkedList<TimeInterval>();
        for (Train train : regions.keySet()) {
            for (Pair<Shape, TimeInterval> pair : regions.get(train)) {
                if (pair.first.intersects(cursor) && !list.contains(pair.second))
                    list.add(pair.second);
            }
        }
        return list;
    }
}
