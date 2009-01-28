package net.parostroj.timetable.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.model.TimeIntervalType;
import net.parostroj.timetable.model.Train;

/**
 * Sort and filter.
 *
 * @author jub
 */
public class TrainSortByNodeFilter {
    public List<Train> sortAndFilter(Collection<Train> trains, Set<Node> nodes) {
        List<Train> result = new ArrayList<Train>();
        for (Train train : trains) {
            for (TimeInterval interval : train.getTimeIntervalList()) {
                if (interval.isNodeOwner() && interval.getType() != TimeIntervalType.NODE_END && nodes.contains(interval.getOwner())) {
                    result.add(train);
                    // break inner cycle
                    break;
                }
            }
        }
        // sort result
        Collections.sort(result, new Comparator<Train>() {

            @Override
            public int compare(Train o1, Train o2) {
                return Integer.valueOf(o1.getStartTime()).compareTo(Integer.valueOf(o2.getStartTime()));
            }
        });

        return result;
    }
}
