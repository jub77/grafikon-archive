package net.parostroj.timetable.output2.html;

import java.util.Locale;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.EndPositionsOutput;
import net.parostroj.timetable.output2.OutputFactory;
import net.parostroj.timetable.output2.StartPositionsOutput;
import net.parostroj.timetable.output2.StationTimetablesOutput;

/**
 * Html output factory.
 *
 * @author jub
 */
public class HtmlOutputFactory extends OutputFactory {

    public HtmlOutputFactory() {
    }

    @Override
    public StartPositionsOutput createStartPositionsOutput(TrainDiagram diagram) {
        return new HtmlStartPositionsOutput(diagram, this.getLocale());
    }

    @Override
    public EndPositionsOutput createEndPositionsOutput(TrainDiagram diagram) {
        return new HtmlEndPositionsOutput(diagram, this.getLocale());
    }

    @Override
    public StationTimetablesOutput creStationTimetablesOutput(TrainDiagram diagram) {
        return new HtmlStationTimetablesOutput(diagram, this.getLocale());
    }

    private Locale getLocale() {
        Locale locale = (Locale) this.getParameter("locale");
        if (locale == null)
            locale = Locale.getDefault();
        return locale;
    }
}
