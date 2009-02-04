package net.parostroj.timetable.output2.html;

import java.util.Locale;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.Output;
import net.parostroj.timetable.output2.OutputFactory;

/**
 * Html output factory.
 *
 * @author jub
 */
public class HtmlOutputFactory extends OutputFactory {

    public HtmlOutputFactory() {
    }

    private Locale getLocale() {
        Locale locale = (Locale) this.getParameter("locale");
        if (locale == null)
            locale = Locale.getDefault();
        return locale;
    }

    @Override
    public Output createOutput(String type) {
        if ("starts".equals(type))
            return new HtmlStartPositionsOutput(this.getLocale());
        else if ("ends".equals(type))
            return new HtmlEndPositionsOutput(this.getLocale());
        else if ("stations".equals(type))
            return new HtmlStationTimetablesOutput(this.getLocale());
        else
            throw new RuntimeException("Unknown type.");
    }
}
