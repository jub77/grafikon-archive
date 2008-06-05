package net.parostroj.timetable.model;

import java.util.List;

/**
 * Penalty table.
 *
 * @author jub
 */
public class PenaltyTable {

    private static final double ADJUST_RATIO = 0.18d;
    private List<PenaltyTableItem> itemList;

    /**
     * Default constructor.
     */
    public PenaltyTable() {
    }

    /**
     * @return the table
     */
    public List<PenaltyTableItem> getItemList() {
        return itemList;
    }

    /**
     * @param table the table to set
     */
    public void setItemList(List<PenaltyTableItem> table) {
        this.itemList = table;
    }

    /**
     * returns braking time penalty (in model seconds).
     *
     * @param type
     * @param velocity
     * @param timeScale
     * @return braking time penalty
     */
    public int getBrakingTimePenalty(SpeedingBrakingType type, int velocity, double timeScale) {
        PenaltyTableItem item = this.getItem(type, velocity);
        return this.adjustByRatio(item.getBrakingPenalty(), timeScale);
    }

    /**
     * returns speeding time penalty (in model seconds).
     *
     * @param type
     * @param velocity
     * @param timeScale
     * @return speeding time penalty
     */
    public int getSpeedingTimePenalty(SpeedingBrakingType type, int velocity, double timeScale) {
        PenaltyTableItem item = this.getItem(type, velocity);
        return this.adjustByRatio(item.getSpeedingPenalty(), timeScale);
    }

    private PenaltyTableItem getItem(SpeedingBrakingType type, int velocity) {
        for (PenaltyTableItem item : itemList) {
            if (type == item.getType() && velocity >= item.getLowerLimit() && velocity < item.getUpperLimit()) {
                return item;
            }
        }
        throw new RuntimeException("Penalty not found.");
    }

    private int adjustByRatio(int penalty, double timeScale) {
        return (int) Math.round(penalty * ADJUST_RATIO * timeScale);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return itemList.toString();
    }
}
