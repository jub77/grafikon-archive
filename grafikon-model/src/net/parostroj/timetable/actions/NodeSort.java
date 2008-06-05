/*
 * NodeSort.java
 * 
 * Created on 8.9.2007, 14:44:24
 */

package net.parostroj.timetable.actions;

import java.text.Collator;
import java.util.*;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.NodeType;

/**
 * Sorting of nodes.
 * 
 * @author jub
 */
public class NodeSort {
    
    public enum Type {ASC, DESC; }
    
    private Type type;

    public NodeSort(Type type) {
        this.type = type;
    }
    
    /**
     * sorts list of nodes.
     * 
     * @param nodes nodes
     * @return sorted list
     */
    public List<Node> sort(Collection<Node> nodes) {
        Comparator<Node> comparator = null;
        List<Node> newNodes = new ArrayList<Node>(nodes);
        switch (type) {
            case ASC:
                comparator = new Comparator<Node>() {
                    private Collator c = Collator.getInstance();
                    @Override
                    public int compare(Node o1, Node o2) {
                        return c.compare(o1.getName(), o2.getName());
                    }
                };
                break;
            case DESC:
                comparator = new Comparator<Node>() {
                    private Collator c = Collator.getInstance();
                    @Override
                    public int compare(Node o1, Node o2) {
                        return c.compare(o2.getName(), o1.getName());
                    }
                };
                break;
        }
        Collections.sort(newNodes, comparator);
        return newNodes;
    }
    
    /**
     * sorts list of nodes and removes signal nodes.
     * 
     * @param nodes collection of nodes
     * @return sorted collections
     */
    public List<Node> sortWithoutSignals(Collection<Node> nodes) {
        List<Node> sorted = this.sort(nodes);
        for (Iterator<Node> i = sorted.iterator(); i.hasNext();) {
            Node n = i.next();
            if (n.getType() == NodeType.SIGNAL)
                i.remove();
        }
        return sorted;
    }
}