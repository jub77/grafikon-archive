package net.parostroj.timetable.output2.html;

import java.util.Locale;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.OutputFactory;
import net.parostroj.timetable.output2.StartPositionsOutput;

/**
 * Html output factory.
 *
 * @author jub
 */
public class HtmlOutputFactory extends OutputFactory {

    private Locale locale;

    public HtmlOutputFactory(Locale locale) {
        this.locale = locale;
    }

    @Override
    public StartPositionsOutput createStartPositionsOutput(TrainDiagram diagram) {
        return new HtmlStartPositionsOutput(diagram, locale);
    }
}
