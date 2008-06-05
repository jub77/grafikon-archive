/*
 * TrainUnitCycleDelegate.java
 * 
 * Created on 16.9.2007, 15:35:44
 */
package net.parostroj.timetable.gui.views;

import java.util.List;
import javax.swing.JComponent;
import net.parostroj.timetable.gui.ApplicationModel;
import net.parostroj.timetable.gui.ApplicationModelEvent;
import net.parostroj.timetable.gui.ApplicationModelEventType;
import net.parostroj.timetable.gui.dialogs.TCDetailsViewDialog;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.ResourceLoader;
import net.parostroj.timetable.utils.TimeConverter;
import net.parostroj.timetable.utils.Tuple;

/**
 * Implementation of the interface for train unit cycle.
 * 
 * @author jub
 */
public class TrainUnitCycleDelegate implements TCDelegate {
    
    private TCDetailsViewDialog editDialog;

    public TrainUnitCycleDelegate() {
    }

    @Override
    public void setSelectedCycle(ApplicationModel model, TrainsCycle cycle) {
        model.setSelectedTrainUnitCycle(cycle);
    }

    @Override
    public TrainsCycle getSelectedCycle(ApplicationModel model) {
        return model.getSelectedTrainUnitCycle();
    }

    @Override
    public void fireEvent(Action action, ApplicationModel model, TrainsCycle cycle) {
        ApplicationModelEvent event = null;
        switch (action) {
        case SELECTED_CHANGED:
            event = new ApplicationModelEvent(ApplicationModelEventType.SELECTED_TRAIN_UNIT_CYCLE_CHANGED, model, cycle);
            break;
        case DELETE_CYCLE:
            event = new ApplicationModelEvent(ApplicationModelEventType.DELETE_TRAIN_UNIT_CYCLE, model, cycle);
            break;
        case MODIFIED_CYCLE:
            event = new ApplicationModelEvent(ApplicationModelEventType.MODIFIED_TRAIN_UNIT_CYCLE, model, cycle);
            break;
        case NEW_CYCLE:
            event = new ApplicationModelEvent(ApplicationModelEventType.NEW_TRAIN_UNIT_CYCLE, model, cycle);
            break;
        }
        if (event != null)
            model.fireEvent(event);
    }

    @Override
    public Action transformEventType(ApplicationModelEventType type) {
        switch (type) {
        case DELETE_TRAIN_UNIT_CYCLE:
            return TCDelegate.Action.DELETE_CYCLE;
        case NEW_TRAIN_UNIT_CYCLE:
            return TCDelegate.Action.NEW_CYCLE;
        case MODIFIED_TRAIN_UNIT_CYCLE:
            return TCDelegate.Action.MODIFIED_CYCLE;
        case SELECTED_TRAIN_UNIT_CYCLE_CHANGED:
            return TCDelegate.Action.SELECTED_CHANGED;
        }
        return null;
    }

    @Override
    public String getTrainCycleErrors(TrainsCycle cycle) {
        StringBuilder result = new StringBuilder();
        List<Tuple<Train>> conflicts = cycle.checkConflicts();
        for (Tuple<Train> item : conflicts) {
            if (item.first.getEndNode() != item.second.getStartNode())
                result.append(String.format(ResourceLoader.getString("ec.problem.nodes"),item.first.getName(),item.first.getEndNode().getName(),item.second.getName(),item.second.getStartNode().getName()));
            else if (item.first.getEndTime() >= item.second.getStartTime())
                result.append(String.format(ResourceLoader.getString("ec.problem.time"),item.first.getName(),TimeConverter.convertFromIntToText(item.first.getEndTime()),item.second.getName(),TimeConverter.convertFromIntToText(item.second.getStartTime())));
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public List<TrainsCycleItem> getTrainCycles(Train train) {
        return train.getCycles(TrainsCycleType.TRAIN_UNIT_CYCLE);
    }

    @Override
    public TrainsCycleType getType() {
        return TrainsCycleType.TRAIN_UNIT_CYCLE;
    }

    @Override
    public void showEditDialog(JComponent component, ApplicationModel model) {
        if (editDialog == null)
            editDialog = new TCDetailsViewDialog((java.awt.Frame)component.getTopLevelAncestor(), true);
        editDialog.setLocationRelativeTo(component);
        editDialog.updateValues(this, model);
        editDialog.setVisible(true);
    }

    @Override
    public String getCycleDescription(ApplicationModel model) {
        return getSelectedCycle(model).getDescription();
    }
}
