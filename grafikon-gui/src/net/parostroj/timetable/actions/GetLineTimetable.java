package net.parostroj.timetable.actions;

import java.util.Formatter;
import net.parostroj.timetable.model.Net;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.model.Line;
import net.parostroj.timetable.model.LineTrack;
import net.parostroj.timetable.utils.TimeConverter;

/**
 * Track occupancy.
 *
 * @author jub
 */
public class GetLineTimetable {

    /**
     * returns text rep. of track occupancy.
     *
     * @param line line
     * @param net net
     * @return text
     */
    public String getTextTimeTable(Line line, Net net) {
        // get end stations ...
        Node ss = net.getEdgeSource(line);
        Node st = net.getEdgeTarget(line);

        StringBuilder builder = new StringBuilder();
        builder.append("Trat: ").append(ss.getName()).append('-').append(st.getName()).append('\n');

        for (LineTrack track : line.getTracks()) {
            for (TimeInterval interval : track.getTimeIntervalList()) {
                Formatter f = new Formatter(builder);
                f.format("%1$-20s", interval.getTrain().toString());
                builder.append(TimeConverter.convertFromIntToTextWS(interval.getStart())).append(" ").append(TimeConverter.convertFromIntToTextWS(interval.getEnd())).append('\n');
            }
        }

        return builder.toString();
    }
}
