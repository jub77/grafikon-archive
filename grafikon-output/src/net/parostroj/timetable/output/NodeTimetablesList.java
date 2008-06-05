/*
 * NodeTimetablesList.java
 * 
 * Created on 11.9.2007, 19:33:20
 */
package net.parostroj.timetable.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import net.parostroj.timetable.actions.NodeSort;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.TimeConverter;

/**
 * List of timetables for nodes.
 * 
 * @author jub
 */
public class NodeTimetablesList {
    
    private List<Node> nodes;
    
    private NodeTimetablesListTemplates templates;

    public NodeTimetablesList(Collection<Node> aNodes) {
        NodeSort s = new NodeSort(NodeSort.Type.ASC);
        nodes = s.sortWithoutSignals(aNodes);
        templates = new NodeTimetablesListTemplates();
    }

    public void writeTo(Writer writer) throws IOException {
        Formatter f = new Formatter(writer);
        writer.write(String.format(templates.getHtmlHeader(),templates.getString("node.timetable")));
        for (Node node : nodes) {
            f.format(templates.getTimetableHeader(), node.getName(),
                templates.getString("column.train"),
                templates.getString("column.from"),
                templates.getString("column.arrival"),
                templates.getString("column.track"),
                templates.getString("column.departure"),
                templates.getString("column.to"),
                templates.getString("column.notes")
              );
            for (TimeInterval i : this.collectIntervals(node)) {
                this.writeLine(f, writer, i);
            }
            writer.write(templates.getTimetableFooter());
        }
        writer.write(templates.getHtmlFooter());
    }
    
    private void writeLine(Formatter f, Writer writer, TimeInterval i) throws IOException {
        TimeInterval from = i.getTrain().getIntervalBefore(i);
        TimeInterval to = i.getTrain().getIntervalAfter(i);
        
        String fromNodeName = TransformUtil.getFromAbbr(i);
        String toNodeName = TransformUtil.getToAbbr(i);
        
        String fromTime = (from == null) ? "&nbsp;" : TimeConverter.convertFromIntToText(i.getStart());
        String toTime = (to == null) ? "&nbsp;" : TimeConverter.convertFromIntToText(i.getEnd());
        
        String comment = this.generateComment(i);
        
        f.format(templates.getTimetableLine(), i.getTrain().getName(),fromNodeName,fromTime,i.getTrack().getNumber(),toTime,toNodeName,comment);
    }
    
    private TimeIntervalList collectIntervals(Node node) {
        TimeIntervalList list = new TimeIntervalList();
        for (NodeTrack track : node.getTracks()) {
            for (TimeInterval i : track.getIntervalList()) {
                list.addIntervalByStartTime(i);
            }
        }
        return list;
    }
    
    private String generateComment(TimeInterval interval) {
        StringBuilder comment = new StringBuilder();
        this.generateCommentForEngineCycle(interval, comment);
        this.generateCommentForTrainUnitCycle(interval, comment);
        // comment itself
        if (interval.getComment() != null && !interval.getComment().equals("")) {
            this.appendDelimiter(comment);
            comment.append(interval.getComment());
        }
        if (Boolean.TRUE.equals(interval.getAttribute("occupied"))) {
            this.appendDelimiter(comment);
            comment.append(TrainTimetablesListTemplates.getString("entry.occupied"));
        }
        if (comment.length() == 0)
            return "&nbsp;";
        else
            return comment.toString();
    }
    
    private void generateCommentForEngineCycle(TimeInterval interval, StringBuilder comment) {
        Train train = interval.getTrain();
        for (TrainsCycleItem item : train.getCycles(TrainsCycleType.ENGINE_CYCLE)) {
            if (item.getToNode() == interval.getOwner()) {
                // end
                TrainsCycleItem itemNext = item.getCycle().getNextItem(item);
                if (itemNext != null) {
                    comment.append(templates.getString("engine.to"));
                    comment.append(' ').append(itemNext.getTrain().getName());
                    comment.append(" (").append(TimeConverter.convertFromIntToText(itemNext.getTrain().getStartTime()));
                    comment.append(')');
                }
            }
            if (item.getFromNode() == interval.getOwner()) {
                // start
                comment.append(templates.getString("engine")).append(": ");
                comment.append(item.getCycle().getName()).append(" (");
                comment.append(item.getCycle().getDescription()).append(')');
            }
        }
    }
    
    private void generateCommentForTrainUnitCycle(TimeInterval interval, StringBuilder comment) {
        Train train = interval.getTrain();
        for (TrainsCycleItem item : train.getCycles(TrainsCycleType.TRAIN_UNIT_CYCLE)) {
            // end
            if (item.getToNode() == interval.getOwner()) {
                TrainsCycleItem itemNext = item.getCycle().getNextItem(item);
                if (itemNext != null) {
                    this.appendDelimiter(comment);
                    comment.append(templates.getString("train.unit.to"));
                    comment.append(' ').append(itemNext.getTrain().getName());
                    comment.append(" (").append(TimeConverter.convertFromIntToText(itemNext.getTrain().getStartTime()));
                    comment.append(')');
                }
            }
            // start
            if (item.getFromNode() == interval.getOwner()) {
                this.appendDelimiter(comment);
                comment.append(templates.getString("train.unit")).append(": ");
                comment.append(item.getCycle().getName()).append(" (");
                comment.append(item.getCycle().getDescription()).append(')');
            }
        }
    }
    
    private void appendDelimiter(StringBuilder str) {
        if (str.length() > 0)
            str.append(", ");
    }
}
