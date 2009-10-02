package net.parostroj.timetable.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Penalty table.
 *
 * @author jub
 */
public class PenaltyTable {

    private Map<TrainTypeCategory, List<PenaltyTableRow>> rowsMap;

    public PenaltyTable() {
        rowsMap = new HashMap<TrainTypeCategory, List<PenaltyTableRow>>();
    }

    public void addRowForCategory(TrainTypeCategory category, PenaltyTableRow row) {
        List<PenaltyTableRow> rows = rowsMap.get(category);
        if (rows == null) {
            rows = new LinkedList<PenaltyTableRow>();
            rowsMap.put(category, rows);
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
}
