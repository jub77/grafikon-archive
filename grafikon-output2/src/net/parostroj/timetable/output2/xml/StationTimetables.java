package net.parostroj.timetable.output2.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import net.parostroj.timetable.output2.impl.StationTimetable;

/**
 * Station timetables.
 *
 * @author jub
 */
@XmlRootElement
public class StationTimetables {

    private List<StationTimetable> timetable;

    public StationTimetables() {
    }

    public StationTimetables(List<StationTimetable> timetable) {
        this.timetable = timetable;
    }

    public List<StationTimetable> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<StationTimetable> timetable) {
        this.timetable = timetable;
    }
}
