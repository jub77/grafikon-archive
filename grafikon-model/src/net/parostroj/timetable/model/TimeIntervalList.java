package net.parostroj.timetable.model;

import java.util.*;

/**
 * Time interval list.
 *
 * @author jub
 */
public class TimeIntervalList extends ArrayList<TimeInterval> {

    /**
     * Default constructor.
     */
    public TimeIntervalList() {
    }

    /**
     * returns time interval for specified train. If the time interval for specified train
     * doesn't exist, it returns <code>null</code>.
     *
     * @param train train
     * @return time interval
     */
    public TimeInterval getTimeInterval(Train train) {
        for (TimeInterval interval : this) {
            if (interval.getTrain().equals(train)) {
                return interval;
            }
        }
        return null;
    }
    
    /**
     * returns time interval for specifed route part. It the time interval
     * doesn't exist, it returns <code>null</code>.
     * 
     * @param owner route part
     * @return searched time interval
     */
    public TimeInterval getTimeInterval(RouteSegment owner) {
        for (TimeInterval interval : this) {
            if (interval.getOwner() == owner)
                return interval;
        }
        return null;
    }
    
    public TimeInterval getIntervalBefore(TimeInterval i) {
        int ind = this.indexOf(i);
        if (ind < 1)
            return null;
        return this.get(ind - 1);
    }
    
    public TimeInterval getIntervalAfter(TimeInterval i) {
        int ind = this.indexOf(i);
        if (ind == -1 || ind > (this.size() - 2))
            return null;
        return this.get(ind + 1);
    }

    /**
     * adds time interval to the list.
     *
     * @param interval time interval do be added
     */
    public void addIntervalForRouteSegment(TimeInterval interval) {
        // update overlapping intervals
        interval.setOverlappingIntervals(this.testIntervalForRouteSegmentOI(interval).getOverlappingIntervals());
        
        // update overlapping intervals in coresponding time intervals
        for (TimeInterval item : interval.getOverlappingIntervals()) {
            item.getOverlappingIntervals().add(interval);
        }
        
        int i = 0;
        for (TimeInterval item : this) {
            if (item.compareOpen(interval) == -1) {
                this.add(i, interval);
                return;
            }
            i++;
        }
        this.add(interval);
    }

    /**
     * adds time interval to the list. It doesn't check overlapping intervals.
     *
     * @param interval time interval do be added
     */
    public void addIntervalForRouteSegmentWithoutCheck(TimeInterval interval) {
        int i = 0;
        for (TimeInterval item : this) {
            if (item.compareOpen(interval) == -1) {
                this.add(i, interval);
                return;
            }
            i++;
        }
        this.add(interval);
    }
    
    public void addIntervalByStartTime(TimeInterval interval) {
        int i=0;
        for (TimeInterval item : this) {
            if (item.getStart() >= interval.getStart()) {
                this.add(i,interval);
                return;
            }
            i++;
        }
        this.add(interval);
    }

    /**
     * adds time interval at the end of list. No testing performed. Usefull when
     * creating train.
     *
     * @param interval interval to be added
     */
    public void addIntervalLastForTrain(TimeInterval interval) {
        this.add(interval);
    }

    /**
     * removes time interval for specified train.
     *
     * @param interval time interval
     */
    public void removeIntervalForRouteSegment(TimeInterval interval) {
        ListIterator<TimeInterval> i = this.listIterator();
        while (i.hasNext()) {
            TimeInterval item = i.next();
            if (item == interval) {
                i.remove();
                break;
            }
        }
        
        // remove itself from other time intervals (overlapping)
        for (TimeInterval item : interval.getOverlappingIntervals()) {
            item.getOverlappingIntervals().remove(interval);
        }
    }

    /**
     * tests if specified time frame is available. It doesn't return ovelapping intervals.
     *
     * @param interval time interval to be tested
     * @return result
     */
    public TimeIntervalResult testIntervalForRouteSegment(TimeInterval interval) {
        for (TimeInterval item : this) {
            if (item.compareOpen(interval) == 0 && !item.equals(interval)) {
                return new TimeIntervalResult(TimeIntervalResult.Status.OVERLAPPING);
            }
        }
        return new TimeIntervalResult(TimeIntervalResult.Status.OK);
    }

    /**
     * tests if specified time interval is available to be added. It returns overlapping intervals.
     *
     * @param interval time interval to be tested
     * @return result
     */
    public TimeIntervalResult testIntervalForRouteSegmentOI(TimeInterval interval) {
        Set<TimeInterval> overlaps = null;
        TimeIntervalResult.Status status = TimeIntervalResult.Status.OK;

        for (TimeInterval item : this) {
            if (item.compareOpen(interval) == 0 && !item.equals(interval)) {
                if (status == TimeIntervalResult.Status.OK) {
                    status = TimeIntervalResult.Status.OVERLAPPING;
                    overlaps = new HashSet<TimeInterval>();
                }
                overlaps.add(item);
            }
        }

        return new TimeIntervalResult(status, overlaps);
    }
    
    /**
     * shifts the whole list of time intervals. Can be used only for train time
     * interval list.
     * 
     * @param timeShift amount of time to be shifted
     * @param attached if the train is in attached state
     */
    public void shift(int timeShift, boolean attached) {
        for (TimeInterval item : this) {
            if (attached)
                item.getOwner().removeTimeInterval(item);
            item.shift(timeShift);
            if (attached)
                item.getOwner().addTimeInterval(item);
        }
    }
    
    /**
     * moves the whole list to specified starting point. Can be used only for train's time
     * interval list.
     * 
     * @param time new starting time
     * @param attached if the train is in attached state
     */
    public void move(int time, boolean attached) {
        TimeInterval first = this.get(0);
        int shift = time - first.getEnd();
        this.shift(shift, attached);
    }
}
