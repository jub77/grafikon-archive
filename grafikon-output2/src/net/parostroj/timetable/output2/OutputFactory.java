package net.parostroj.timetable.output2;

import java.util.HashMap;
import java.util.Map;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.html.HtmlOutputFactory;
import net.parostroj.timetable.output2.pdf.PdfOutputFactory;
import net.parostroj.timetable.output2.xml.XmlOutputFactory;

/**
 * Output factory.
 *
 * @author jub
 */
public abstract class OutputFactory {

    private Map<String, Object> parameters = new HashMap<String, Object>();

    /**
     * creates factory.
     *
     * @param type type of the factory
     * @param locale locale
     * @return factory
     */
    public static OutputFactory newInstance(String type) {
        if ("html".equals(type))
            return new HtmlOutputFactory();
        else if ("xml".equals(type))
            return new XmlOutputFactory();
        else if ("pdf".equals(type))
            return new PdfOutputFactory();
        else
            throw new IllegalArgumentException("Unknown output factory type: " + type);
    }

    public abstract Output createOutput(String type, TrainDiagram diagram);

    public void setParameter(String key, Object value) {
        this.parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return this.parameters.get(key);
    }
}
