package net.parostroj.timetable.actions;

import java.util.Formatter;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.utils.TimeConverter;

/**
 * Creates representation of train timetable.
 *
 * @author jub
 */
public class GetTrainTimetable {

    /**
     * creates textual representation of the timetable for the train
     *
     * @param train train
     * @return timetable in textual form.
     */
    public String getTextTimeTable(Train train) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(train).append('\n');
        for (TimeInterval time : train.getTimeIntervalList()) {
            Node node = time.getOwner().asNode();
            if (node != null) {
                Formatter f = new Formatter(buffer);
                f.format("%1$-20s", node.getName());
                if (time.isFirst() || !time.isStop()) {
                    buffer.append(" ").append("     ").append(" ").append(TimeConverter.convertFromIntToTextWS(time.getEnd())).append("\n");
                } else if (time.isLast()) {
                    buffer.append(" ").append(TimeConverter.convertFromIntToTextWS(time.getStart())).append("\n");
                } else if (time.isStop()) {
                    buffer.append(" ").append(TimeConverter.convertFromIntToTextWS(time.getStart())).append(" ").append(TimeConverter.convertFromIntToTextWS(time.getEnd())).append("\n");
                } else {
                    buffer.append('\n');
                }
            }
        }
        return buffer.toString();
    }
}
