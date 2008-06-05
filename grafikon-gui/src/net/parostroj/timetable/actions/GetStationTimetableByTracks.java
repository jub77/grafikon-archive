package net.parostroj.timetable.actions;

import java.util.Formatter;
import java.util.List;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.NodeTrack;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.utils.TimeConverter;

/**
 * Creates representation of train timetable.
 *
 * @author jub
 */
public class GetStationTimetableByTracks {

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

        for (NodeTrack track : tracks) {
            buffer.append("Kolej: " + track.getNumber()).append('\n');
            for (TimeInterval interval : track.getIntervalList()) {
                Formatter f = new Formatter(buffer);
                f.format("%1$-20s", interval.getTrain().toString());
                buffer.append(" ").append(TimeConverter.convertFromIntToTextWS(interval.getStart())).append(" ").append(TimeConverter.convertFromIntToTextWS(interval.getEnd())).append("\n");
            }
        }

        return buffer.toString();
    }
}
