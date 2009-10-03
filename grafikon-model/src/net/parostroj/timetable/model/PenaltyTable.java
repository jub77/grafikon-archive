package net.parostroj.timetable.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import net.parostroj.timetable.utils.IdGenerator;

/**
 * Penalty table.
 *
 * @author jub
 */
public class PenaltyTable implements ObjectWithId {

    private String id;
    private Map<TrainTypeCategory, List<PenaltyTableRow>> rowsMap;

    public PenaltyTable() {
        rowsMap = new HashMap<TrainTypeCategory, List<PenaltyTableRow>>();
        id = IdGenerator.getInstance().getId();
        // fill default categories
        for (TrainTypeCategory cat : TrainTypeCategory.getPredefined()) {
            this.addTrainTypeCategory(cat);
        }
    }

    public void addRowForCategory(TrainTypeCategory category, PenaltyTableRow row) {
        List<PenaltyTableRow> rows = rowsMap.get(category);
        if (rows == null) {
            throw new IllegalStateException("Category doesn't exist.");
        }
        ListIterator<PenaltyTableRow> i = rows.listIterator();
        while (i.hasNext()) {
            PenaltyTableRow currentRow = i.next();
            if (row.getSpeed() < currentRow.getSpeed()) {
                i.previous();
                i.add(row);
                return;
            }
        }
        rows.add(row);
    }

    public void removeRowForSpeedAndCategory(TrainTypeCategory category, int speed) {
        List<PenaltyTableRow> rows = rowsMap.get(category);
        if (rows != null)
            for (Iterator<PenaltyTableRow> i = rows.iterator(); i.hasNext();) {
                PenaltyTableRow row = i.next();
                if (row.getSpeed() == speed) {
                    i.remove();
                }
            }
    }

    public void removeRowForCategory(TrainTypeCategory category, int position) {
        List<PenaltyTableRow> rows = rowsMap.get(category);
        if (rows != null)
            rows.remove(position);
    }

    public PenaltyTableRow getRowForSpeedAndCategory(TrainTypeCategory category, int speed) {
        List<PenaltyTableRow> rows = rowsMap.get(category);
        if (rows != null) {
            ListIterator<PenaltyTableRow> i = rows.listIterator();
            while (i.hasNext()) {
                PenaltyTableRow row = i.next();
                if (speed <= row.getSpeed()) {
                    return row;
                }
            }
        }
        // otherwise return null
        return null;
    }

    public PenaltyTableRow getForSpeedExactAndCategory(TrainTypeCategory category, int speed) {
        List<PenaltyTableRow> rows = rowsMap.get(category);
        if (rows != null) {
            ListIterator<PenaltyTableRow> i = rows.listIterator();
            while (i.hasNext()) {
                PenaltyTableRow row = i.next();
                if (speed == row.getSpeed()) {
                    return row;
                }
            }
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    public TrainTypeCategory getTrainTypeCategory(String categoryString) {
        TrainTypeCategory searched = TrainTypeCategory.fromString(categoryString);
        for (TrainTypeCategory category : rowsMap.keySet()) {
            if (category.equals(searched)) {
                return category;
            }
        }
        return null;
    }

    public void addTrainTypeCategory(TrainTypeCategory category) {
        if (!rowsMap.containsKey(category)) {
            rowsMap.put(category, new LinkedList<PenaltyTableRow>());
        }
    }

    public void removeTrainTypeCategory(TrainTypeCategory category) {
        rowsMap.remove(category);
    }

    public Set<TrainTypeCategory> getTrainTypeCategories() {
        return Collections.unmodifiableSet(rowsMap.keySet());
    }

    public List<PenaltyTableRow> getPenaltyTableRowsForCategory(TrainTypeCategory category) {
        List<PenaltyTableRow> row = rowsMap.get(category);
        if (row == null)
            return null;
        else
            return Collections.unmodifiableList(row);
    }
}
