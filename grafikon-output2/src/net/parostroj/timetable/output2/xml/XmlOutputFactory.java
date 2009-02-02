package net.parostroj.timetable.output2.xml;

import java.nio.charset.Charset;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.Output;
import net.parostroj.timetable.output2.OutputFactory;

/**
 * Xml output factory.
 *
 * @author jub
 */
public class XmlOutputFactory extends OutputFactory {

    private Charset getCharset() {
        Charset charset = (Charset) this.getParameter("charset");
        if (charset == null) {
            charset = Charset.forName("utf-8");
        }
        return charset;
    }

    @Override
    public Output createOutput(String type, TrainDiagram diagram) {
        if ("starts".equals(type))
            return new XmlStartPositionsOutput(diagram, this.getCharset());
        else if ("ends".equals(type))
            return new XmlEndPositionsOutput(diagram, this.getCharset());
        else if ("stations".equals(type))
            return new XmlStationTimetablesOutput(diagram, this.getCharset());
        else
            throw new RuntimeException("Unknown type.");
    }
}
