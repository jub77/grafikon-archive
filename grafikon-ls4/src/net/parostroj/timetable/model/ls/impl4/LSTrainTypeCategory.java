package net.parostroj.timetable.model.ls.impl4;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import net.parostroj.timetable.model.PenaltyTableRow;
import net.parostroj.timetable.model.TrainTypeCategory;

/**
 * Storage for train type category.
 *
 * @author jub
 */
@XmlType(propOrder = {"key", "rows"})
public class LSTrainTypeCategory {

    private String key;
    private List<LSPenaltyTableRow> rows;

    public LSTrainTypeCategory() {
    }

    public LSTrainTypeCategory(TrainTypeCategory category, List<PenaltyTableRow> penaltyRows) {
        this.key = category.getKey();
        this.rows = new LinkedList<LSPenaltyTableRow>();
        for (PenaltyTableRow r : penaltyRows) {
            rows.add(new LSPenaltyTableRow(r));
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlElementWrapper(name = "penalty_rows")
    @XmlElement(name = "row")
    public List<LSPenaltyTableRow> getRows() {
        return rows;
    }

    public void setRows(List<LSPenaltyTableRow> rows) {
        this.rows = rows;
    }

    public TrainTypeCategory createTrainTypeCategory() {
        return TrainTypeCategory.fromString(key);
    }
}
