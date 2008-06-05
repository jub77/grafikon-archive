package net.parostroj.timetable.output;

import java.util.List;
import java.util.ListIterator;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.NodeType;
import net.parostroj.timetable.model.TimeInterval;

/**
 * Transformation of texts.
 * 
 * @author jub
 */
public class TransformUtil {
    public static String transformStation(Node node) {
        String name = node.getName();
        if (node.getType() == NodeType.STOP)
            name += " " + TrainTimetablesListTemplates.getString("abbr.stop");
        else if (node.getType() == NodeType.STOP_WITH_FREIGHT)
            name += " " + TrainTimetablesListTemplates.getString("abbr.stop.freight");
        return name;
    }
    
    public static String getFromAbbr(TimeInterval i) {
        Node node = null;
        for (TimeInterval current : i.getTrain().getTimeIntervalList()) {
            if (current == i)
                break;
            if (current.getOwner() instanceof Node) {
                Node n = (Node)current.getOwner();
                switch (n.getType()) {
                    case STOP: case STOP_WITH_FREIGHT: case ROUTE_SPLIT: case SIGNAL:
                        // do nothing
                        break;
                    default:
                        node = n;
                        break;
                }
            }
        }
        if (node == null)
            return "&nbsp;";
        else
            return node.getAbbr();
    }
    
    public static String getToAbbr(TimeInterval i) {
        Node node = null;
        List<TimeInterval> list = i.getTrain().getTimeIntervalList();
        ListIterator<TimeInterval> iterator = list.listIterator(list.size());
        while (iterator.hasPrevious()) {
            TimeInterval current = iterator.previous();
            if (current == i)
                break;
            if (current.getOwner() instanceof Node) {
                Node n = (Node)current.getOwner();
                switch (n.getType()) {
                    case STOP: case STOP_WITH_FREIGHT: case ROUTE_SPLIT: case SIGNAL:
                        // do nothing
                        break;
                    default:
                        node = n;
                        break;
                }
            }
        }
        if (node == null)
            return "&nbsp;";
        else
            return node.getAbbr();
    }
}
