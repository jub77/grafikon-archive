package net.parostroj.timetable.gui.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;

/**
 * List model with wrappers around object with ids.
 *
 * @author jub
 */
public class WrapperListModel extends AbstractListModel {

    private Set<Object> set;
    private List<Wrapper<?>> list;
    private boolean sorted;

    public WrapperListModel() {
        list = new ArrayList<Wrapper<?>>();
        sorted = true;
    }

    public WrapperListModel(List<Wrapper<?>> list) {
        this.list = list;
        this.sorted = true;
        this.sort(list);
    }

    public WrapperListModel(List<Wrapper<?>> list, Set<Object> set) {
        this.list = list;
        this.set = set;
        this.sorted = true;
        this.sort(list);
    }

    public WrapperListModel(List<Wrapper<?>> list, Set<Object> set, boolean sorted) {
        this.list = list;
        this.set = set;
        this.sorted = sorted;
        this.sort(list);
    }

    private void sort(List<? extends Wrapper<?>> ll) {
        if (sorted)
            Collections.sort(ll);
    }

    public void removeWrapper(Wrapper<?> w) {
        // remove from set
        if (set != null)
            set.remove(w.getElement());
        // remove from list
        int index = list.indexOf(w);
        if (index != -1) {
            list.remove(w);
            this.fireIntervalRemoved(this, index, index);
        }
    }

    public void addWrapper(Wrapper<?> w) {
        // add to set
        if (set != null)
            set.add(w.getElement());
        // add to list
        list.add(w);
        this.sort(list);
        int index = list.indexOf(w);
        this.fireIntervalAdded(this, index, index);
    }

    public List<Wrapper<?>> getListOfWrappers() {
        return list;
    }

    public Set<Object> getSetOfObjects() {
        return set;
    }

    public void initializeSet() {
        this.set = new HashSet<Object>();
        for (Wrapper<?> w : list) {
            this.set.add(w.getElement());
        }
    }

    public void setListOfWrappers(List<Wrapper<?>> list) {
        this.fireIntervalRemoved(this, 0, list.size() - 1);
        this.list = list;
        this.set = null;
        this.fireIntervalAdded(this, 0, list.size() - 1);
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
