package net.parostroj.timetable.model;

/**
 * Penalty table item.
 *
 * @author jub
 */
public class PenaltyTableItem {

    private int lowerLimit;
    private int upperLimit;
    private SpeedingBrakingType type;
    private int brakingPenalty;
    private int speedingPenalty;

    /**
     * @return the brakingPenalty
     */
    public int getBrakingPenalty() {
        return brakingPenalty;
    }

    /**
     * @param brakingPenalty the brakingPenalty to set
     */
    public void setBrakingPenalty(int brakingPenalty) {
        this.brakingPenalty = brakingPenalty;
    }

    /**
     * @return the lowerLimit
     */
    public int getLowerLimit() {
        return lowerLimit;
    }

    /**
     * @param lowerLimit the lowerLimit to set
     */
    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /**
     * @return the speedingPenalty
     */
    public int getSpeedingPenalty() {
        return speedingPenalty;
    }

    /**
     * @param speedingPenalty the speedingPenalty to set
     */
    public void setSpeedingPenalty(int speedingPenalty) {
        this.speedingPenalty = speedingPenalty;
    }

    /**
     * @return the trainType
     */
    public SpeedingBrakingType getType() {
        return type;
    }

    /**
     * @param trainType the trainType to set
     */
    public void setType(SpeedingBrakingType trainType) {
        this.type = trainType;
    }

    /**
     * @return the upperLimit
     */
    public int getUpperLimit() {
        return upperLimit;
    }

    /**
     * @param upperLimit the upperLimit to set
     */
    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    /**
     * @param lowerLimit
     * @param upperLimit
     * @param type
     * @param brakingPenalty
     * @param speedingPenalty
     */
    public PenaltyTableItem(int lowerLimit, int upperLimit, SpeedingBrakingType type, int brakingPenalty, int speedingPenalty) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.type = type;
        this.brakingPenalty = brakingPenalty;
        this.speedingPenalty = speedingPenalty;
    }

    /**
     * Default constructor.
     */
    public PenaltyTableItem() {
    }

    @Override
    public String toString() {
        return "<" + lowerLimit + "," + upperLimit + "," + type + "," + speedingPenalty + "," + brakingPenalty + ">";
    }
}