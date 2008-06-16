package net.parostroj.timetable.actions;

import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.NodeTrack;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.utils.TimeConverter;
import net.parostroj.timetable.utils.Pair;

/**
 * Creates representation of train timetable.
 *
 * @author jub
 */
public class GetStationTimetable {

    /**
     * creates textual representation of the timetable for the station.
     *
     * @param station station
     * @return timetable in textual form.
     */
    public String getTextTimeTable(Node station) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(station).append('\n');
        List<NodeTrack> tracks = station.getTracks();

        List<Pair<TimeInterval, String>> list = new LinkedList<Pair<TimeInterval, String>>();

        for (NodeTrack track : tracks) {
            for (TimeInterval interval : track.getTimeIntervalList()) {
                this.addTimeInterval(list, interval, track.getNumber());
            }
        }

        for (Pair<TimeInterval, String> item : list) {
            Formatter f = new Formatter(buffer);
            f.format("%1$-20s", item.first.getTrain());
            buffer.append(" ").append(TimeConverter.convertFromIntToTextWS(item.first.getStart())).append(" ").append(TimeConverter.convertFromIntToTextWS(item.first.getEnd())).append(" [kolej: ").append(item.second).append("]\n");
        }
        return buffer.toString();
    }

    private void addTimeInterval(List<Pair<TimeInterval, String>> list, TimeInterval interval, String trackNumber) {
        ListIterator<Pair<TimeInterval, String>> i = list.listIterator();
        while (i.hasNext()) {
            Pair<TimeInterval, String> current = i.next();
            if (current.first.getStart() > interval.getStart()) {
                i.previous();
                i.add(new Pair<TimeInterval, String>(interval, trackNumber));
                return;
            }
        }
        // add at the end
        list.add(new Pair<TimeInterval, String>(interval, trackNumber));
    }
}
