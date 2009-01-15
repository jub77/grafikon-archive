package net.parostroj.timetable.output2.html;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.StationTimetablesOutput;

/**
 * Implements html output for station timetable.
 *
 * @author jub
 */
public class HtmlStationTimetablesOutput implements StationTimetablesOutput {

    private TrainDiagram diagram;

    private Locale locale;

    HtmlStationTimetablesOutput(TrainDiagram diagram, Locale locale) {
        this.locale = locale;
        this.diagram = diagram;
    }

    @Override
    public void writeTo(Writer writer) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
