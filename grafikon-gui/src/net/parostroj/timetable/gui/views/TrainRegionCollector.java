package net.parostroj.timetable.gui.views;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.parostroj.timetable.model.Train;

/**
 * Class for collecting regions of the trains from graphical timetable.
 * 
 * @author jub
 */
public class TrainRegionCollector {
    
    private Map<Train,List<Shape>> regions;
    
    private boolean collected;
    
    private Train modifiedTrain;
    
    // sensitivity radius
    private int radius;

    public TrainRegionCollector(int radius) {
        regions = new HashMap<Train, List<Shape>>();
        collected = false;
        modifiedTrain = null;
        this.radius = radius;
    }
    
    public void addRegion(Train train,Shape shape) {
        // check if the regions are already collected
        if (collected && modifiedTrain != train)
            return;
        // create key if doesn't exist
        if (!regions.containsKey(train)) {
            regions.put(train,new LinkedList<Shape>());
        }
        // add shape
        regions.get(train).add(shape);
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
    
    public List<Train> getTrainForPoint(int x, int y) {
        Rectangle2D cursor = new Rectangle2D.Double(x - radius, y - radius, radius * 2, radius * 2);
        List<Train> list = new LinkedList<Train>();
        for (Train train : regions.keySet()) {
            for (Shape shape : regions.get(train)) {
                if (shape.intersects(cursor) && !list.contains(train))
                    list.add(train);
            }
        }
        return list;
    }
}
