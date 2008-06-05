/*
 * EngineCycleDelegate.java
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
import net.parostroj.timetable.gui.dialogs.TCDetailsViewDialogEngineClass;
import net.parostroj.timetable.model.EngineClass;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.model.TrainsCycle;
import net.parostroj.timetable.model.TrainsCycleItem;
import net.parostroj.timetable.model.TrainsCycleType;
import net.parostroj.timetable.utils.ResourceLoader;
import net.parostroj.timetable.utils.TimeConverter;
import net.parostroj.timetable.utils.Tuple;

/**
 * Implementation of the interface for engine cycle.
 * 
 * @author jub
 */
public class EngineCycleDelegate implements TCDelegate {
    
    private TCDetailsViewDialogEngineClass editDialog;

    public EngineCycleDelegate() {
    }

    @Override
    public void setSelectedCycle(ApplicationModel model, TrainsCycle cycle) {
        model.setSelectedEngineCycle(cycle);
    }

    @Override
    public TrainsCycle getSelectedCycle(ApplicationModel model) {
        return model.getSelectedEngineCycle();
    }

    @Override
    public void fireEvent(Action action, ApplicationModel model, TrainsCycle cycle) {
        ApplicationModelEvent event = null;
        switch (action) {
        case SELECTED_CHANGED:
            event = new ApplicationModelEvent(ApplicationModelEventType.SELECTED_ENGINE_CYCLE_CHANGED, model, cycle);
            break;
        case DELETE_CYCLE:
            event = new ApplicationModelEvent(ApplicationModelEventType.DELETE_ENGINE_CYCLE, model, cycle);
            break;
        case MODIFIED_CYCLE:
            event = new ApplicationModelEvent(ApplicationModelEventType.MODIFIED_ENGINE_CYCLE, model, cycle);
            break;
        case NEW_CYCLE:
            event = new ApplicationModelEvent(ApplicationModelEventType.NEW_ENGINE_CYCLE, model, cycle);
            break;
        }
        if (event != null)
            model.fireEvent(event);
    }

    @Override
    public Action transformEventType(ApplicationModelEventType type) {
        switch (type) {
        case DELETE_ENGINE_CYCLE:
            return TCDelegate.Action.DELETE_CYCLE;
        case NEW_ENGINE_CYCLE:
            return TCDelegate.Action.NEW_CYCLE;
        case MODIFIED_ENGINE_CYCLE:
            return TCDelegate.Action.MODIFIED_CYCLE;
        case SELECTED_ENGINE_CYCLE_CHANGED:
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
        return train.getCycles(TrainsCycleType.ENGINE_CYCLE);
    }

    @Override
    public TrainsCycleType getType() {
        return TrainsCycleType.ENGINE_CYCLE;
    }

    @Override
    public void showEditDialog(JComponent component, ApplicationModel model) {
        if (editDialog == null)
            editDialog = new TCDetailsViewDialogEngineClass((java.awt.Frame)component.getTopLevelAncestor(), true);
        editDialog.setLocationRelativeTo(component);
        editDialog.updateValues(this, model);
        editDialog.setVisible(true);
    }

    @Override
    public String getCycleDescription(ApplicationModel model) {
        TrainsCycle cycle = getSelectedCycle(model);
        StringBuilder b = new StringBuilder();
        if (cycle.getDescription() != null)
            b.append(cycle.getDescription());
        if (cycle.getAttribute("engine.class") != null) {
            if (b.length() != 0)
                b.append(' ');
            b.append('[').append(((EngineClass)cycle.getAttribute("engine.class")).getName()).append(']');
        }
        return b.toString();
    }
}