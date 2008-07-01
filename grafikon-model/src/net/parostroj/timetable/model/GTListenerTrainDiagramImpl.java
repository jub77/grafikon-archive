package net.parostroj.timetable.model;

import java.util.logging.Logger;
import net.parostroj.timetable.model.events.NetEvent;
import net.parostroj.timetable.model.events.NetListener;
import net.parostroj.timetable.model.events.TrainEvent;
import net.parostroj.timetable.model.events.TrainListener;
import net.parostroj.timetable.model.events.TrainsCycleEvent;
import net.parostroj.timetable.model.events.TrainsCycleListener;

/**
 * Listener implementation for train diagram.
 * 
 * @author jub
 */
class GTListenerTrainDiagramImpl implements TrainListener, TrainsCycleListener, NetListener {

    private static final Logger LOG = Logger.getLogger(GTListenerTrainDiagramImpl.class.getName());
    private TrainDiagram diagram;

    public GTListenerTrainDiagramImpl(TrainDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void trainChanged(TrainEvent event) {
        LOG.fine(event.toString());
    }

    @Override
    public void trainsCycleChanged(TrainsCycleEvent event) {
        LOG.fine(event.toString());
    }

    @Override
    public void netChanged(NetEvent event) {
        LOG.fine(event.toString());
    }
}
