package net.parostroj.timetable.model;

import java.util.logging.Logger;
import net.parostroj.timetable.model.events.TrainEvent;
import net.parostroj.timetable.model.events.TrainListener;

/**
 * Listener implementation for train diagram.
 * 
 * @author jub
 */
class GTListenerTrainDiagramImpl implements TrainListener {

    private static final Logger LOG = Logger.getLogger(GTListenerTrainDiagramImpl.class.getName());
    private TrainDiagram diagram;

    public GTListenerTrainDiagramImpl(TrainDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void trainChanged(TrainEvent event) {
        LOG.fine(event.toString());
    }
}
