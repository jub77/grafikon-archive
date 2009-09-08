package net.parostroj.timetable.gui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;
import net.parostroj.timetable.gui.helpers.Wrapper;
import net.parostroj.timetable.model.TrainDiagram;

/**
 * List model with wrappers around object with ids.
 *
 * @author jub
 */
public class WrapperListModel extends AbstractListModel {

    private Set<Object> set;
    private List<Wrapper<?>> list;
    private ImportComponents comp;

    public WrapperListModel(Set<Object> set, ImportComponents comp, TrainDiagram diagram) {
        this.set = set;
        this.comp = comp;
        list = new ArrayList<Wrapper<?>>(set.size());
        for (Object oid : set) {
            list.add(comp.getWrapper(oid, diagram));
        }
        this.sort(list);
    }

    private void sort(List<? extends Wrapper<?>> ll) {
        Collections.sort(ll);
    }

    public void removeWrapper(Wrapper<?> w) {
        // remove from set
        set.remove(w.getElement());
        // remove from list
        int index = list.indexOf(w);
        if (index != -1) {
            list.remove(w);
            this.fireIntervalRemoved(list, index, index);
        }
    }

    public void addWrapper(Wrapper<?> w) {
        // add to set
        set.add(w.getElement());
        // add to list
        list.add(w);
        this.sort(list);
        int index = list.indexOf(w);
        this.fireIntervalAdded(set, index, index);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }
}
