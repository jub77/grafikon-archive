package net.parostroj.timetable.gui.dialogs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.parostroj.timetable.actions.TrainComparator;
import net.parostroj.timetable.gui.helpers.NodeWrapper;
import net.parostroj.timetable.gui.helpers.TrainWrapper;
import net.parostroj.timetable.gui.helpers.TrainsTypeWrapper;
import net.parostroj.timetable.gui.helpers.Wrapper;
import net.parostroj.timetable.gui.utils.ResourceLoader;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.model.TrainType;

/**
 * Export/Import components.
 *
 * @author jub
 */
public enum ImportComponents {
    TRAINS("import.trains"),
    NODES("import.stations"),
    TRAIN_TYPES("import.train_types");

    private String key;

    private ImportComponents(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return ResourceLoader.getString(key);
    }

    public Set<Object> getObjects(TrainDiagram diagram) {
        if (diagram == null)
            return Collections.emptySet();
        Set<Object> map = new HashSet<Object>();
        switch (this) {
            case NODES:
                map.addAll(diagram.getNet().getNodes());
                break;
            case TRAINS:
                map.addAll(diagram.getTrains());
                break;
            case TRAIN_TYPES:
                map.addAll(diagram.getTrainTypes());
                break;
        }
        return map;
    }

    public Wrapper<?> getWrapper(Object oid, TrainDiagram diagram) {
        Wrapper<?> w = null;
        switch (this) {
            case NODES:
                if (oid instanceof Node) {
                    w = new NodeWrapper((Node)oid);
                }
                break;
            case TRAINS:
                if (oid instanceof Train) {
                    w = new TrainWrapper(
                            (Train) oid,
                            TrainWrapper.Type.NAME,
                            new TrainComparator(TrainComparator.Type.ASC, diagram.getTrainsData().getTrainSortPattern()));
                }
                break;
            case TRAIN_TYPES:
                if (oid instanceof TrainType) {
                    w = new TrainsTypeWrapper((TrainType)oid);
                }
                break;
        }
        return w;
    }
}
