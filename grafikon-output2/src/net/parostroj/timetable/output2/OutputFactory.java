package net.parostroj.timetable.output2;

import java.util.Locale;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.html.HtmlOutputFactory;
import net.parostroj.timetable.output2.xml.XmlOutputFactory;

/**
 * Output factory.
 *
 * @author jub
 */
public abstract class OutputFactory {

    /**
     * creates factory.
     *
     * @param type type of the factory
     * @param locale locale
     * @return factory
     */
    public static OutputFactory newInstance(String type, Locale locale) {
        if ("html".equals(type))
            return new HtmlOutputFactory(locale);
        else if ("xml".equals(type))
            return new XmlOutputFactory();
        else
            throw new IllegalArgumentException("Unknown output factory type: " + type);
    }

    public static OutputFactory newInstance(String type) {
        return newInstance(type, Locale.getDefault());
    }

    public abstract StartPositionsOutput createStartPositionsOutput(TrainDiagram diagram);

    public abstract EndPositionsOutput createEndPositionsOutput(TrainDiagram diagram);
}
