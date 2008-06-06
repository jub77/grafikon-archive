/*
 * EngineRun.java
 * 
 * Created on 11.9.2007, 20:30:58
 */
package net.parostroj.timetable.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.parostroj.timetable.utils.Tuple;

/**
 * Run of engine through trains.
 * 
 * @author jub
 */
public class TrainsCycle implements AttributesHolder, ObjectWithId, Iterable<TrainsCycleItem> {

    private final String id;
    private String name;
    private String description;
    private TrainsCycleType type;
    private Attributes attributes;
    private List<TrainsCycleItem> items;

    /**
     * creates instance
     * 
     * @param id id
     * @param name name of the cycle
     * @param description description
     */
    public TrainsCycle(String id, String name, String description, TrainsCycleType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.attributes = new Attributes();
        this.type = type;
        this.items = new LinkedList<TrainsCycleItem>();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id != null ? this.id.hashCode() : 0;
        hash = 97 * hash + this.name != null ? this.name.hashCode() : 0;
        hash = 97 * hash + this.description != null ? this.description.hashCode() : 0;
        return hash;
    }

    public List<Tuple<TrainsCycleItem>> checkConflicts() {
        List<Tuple<TrainsCycleItem>> conflicts = null;
        Iterator<TrainsCycleItem> i = items.iterator();
        TrainsCycleItem last = null;
        if (i.hasNext()) {
            last = i.next();
        }
        while (i.hasNext()) {
            TrainsCycleItem current = i.next();
            if (last.getToNode() != current.getFromNode() || last.getEndTime() >= current.getStartTime()) {
                if (conflicts == null) {
                    conflicts = new LinkedList<Tuple<TrainsCycleItem>>();
                }
                conflicts.add(new Tuple<TrainsCycleItem>(last, current));
            }
            last = current;
        }
        if (conflicts == null) {
            conflicts = Collections.emptyList();
        }
        return conflicts;
    }

    public TrainsCycleItem getNextItem(TrainsCycleItem item) {
        int ind = items.indexOf(item);
        if (ind == -1) {
            return null;
        }
        ind++;
        if (ind >= items.size()) {
            return null;
        }
        return items.get(ind);
    }

    public TrainsCycleItem getPreviousItem(TrainsCycleItem item) {
        int ind = items.indexOf(item);
        if (ind == -1) {
            return null;
        }
        ind--;
        if (ind < 0) {
            return null;
        }
        return items.get(ind);
    }

    public void addItem(TrainsCycleItem item) {
        items.add(item);
    }
    
    public void removeItem(TrainsCycleItem item) {
        items.remove(item);
    }
    
    public void addItem(TrainsCycleItem item, int index) {
        items.add(index, item);
    }
    
    public TrainsCycleItem removeItem(int index) {
        return items.remove(index);
    }
    
    public List<TrainsCycleItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public Object removeAttribute(String key) {
        return attributes.remove(key);
    }

    public TrainsCycleType getType() {
        return type;
    }

    public void setType(TrainsCycleType type) {
        this.type = type;
    }

    @Override
    public Iterator<TrainsCycleItem> iterator() {
        return items.iterator();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
