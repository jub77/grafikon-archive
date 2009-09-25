package net.parostroj.timetable.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.parostroj.timetable.utils.TimeConverter;

/**
 * Time interval. It holds two values - start and end. Immutable object.
 *
 * @author jub
 */
final public class Interval {
    private final int start;
    private final int end;

    public Interval(TimeInterval timeInterval) {
        this.start = timeInterval.getStart();
        this.end = timeInterval.getEnd();
    }

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return end - start;
    }

    public Interval normalize() {
        int normStart = TimeConverter.normalizeTime(start);
        return new Interval(normStart, normStart + getLength());
    }

    /**
     * The interval is normalized if the start time of the interval is
     * within bounds 0 - (DAY - 1).
     *
     * @return if the interval is normalized
     */
    public boolean isNormalized() {
        return TimeConverter.isNormalizedTime(start);
    }

    /**
     * @return if the interval is over midnight
     */
    public boolean isOverMidnight() {
        int normStart = TimeConverter.normalizeTime(start);
        int compEnd = normStart + (end - start);
        return TimeConverter.isNormalizedTime(compEnd);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Interval other = (Interval) obj;
        if (this.start != other.start) {
            return false;
        }
        if (this.end != other.end) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.start;
        hash = 67 * hash + this.end;
        return hash;
    }

    public int compareOpen(Interval o) {
        if (o.getEnd() < this.getStart()) {
            return -1;
        }
        if (o.getStart() > this.getEnd()) {
            return 1;
        }
        return 0;
    }

    public int compareClosed(Interval o) {
        if (o.getEnd() <= this.getStart()) {
            return -1;
        }
        if (o.getStart() >= this.getEnd()) {
            return 1;
        }
        return 0;
    }

    /**
     * computes normalized intervals (for intervals over midnight it
     * returns two). The first one is always with start time normalized.
     *
     * @return list of normalized intervals
     */
    public List<Interval> computeNormalizedIntervalsAll() {
        List<Interval> list = this.computeNormalizedIntervals();
        if (list == null)
            return Collections.singletonList(this);
        else
            return list;
    }

    /**
     * computes list of normalized intervals (for intervals over midnight
     * it return two intervals). The first one is always with start time
     * normalized. In case the interval is already normalized it returns
     * <code>null</code>.
     *
     * @return list of normalized intervals or <code>null</code> if there
     * is no change
     */
    public List<Interval> computeNormalizedIntervals() {
        // compute
        if (TimeConverter.isNormalizedTime(this.getStart()) &&
                TimeConverter.isNormalizedTime(this.getEnd()))
            // no adjustment needed
            return null;
        else {
            Interval normalized = this.normalize();
            if (TimeConverter.isNormalizedTime(normalized.getEnd())) {
                // interval is not over midnight - only move ...
                return Collections.singletonList(normalized);
            } else {
                // interval over midnight - the result is two intervals ...
                List<Interval> intervals = new ArrayList<Interval>(2);
                intervals.add(normalized);
                intervals.add(new Interval(normalized.getStart() - TimeInterval.DAY, normalized.getEnd() - TimeInterval.DAY));
                return intervals;
            }
        }
    }
}
