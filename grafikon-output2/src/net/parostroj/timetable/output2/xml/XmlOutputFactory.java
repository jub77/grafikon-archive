package net.parostroj.timetable.output2.xml;

import java.nio.charset.Charset;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.EndPositionsOutput;
import net.parostroj.timetable.output2.OutputFactory;
import net.parostroj.timetable.output2.StartPositionsOutput;
import net.parostroj.timetable.output2.StationTimetablesOutput;

/**
 * Xml output factory.
 *
 * @author jub
 */
public class XmlOutputFactory extends OutputFactory {

    @Override
    public StartPositionsOutput createStartPositionsOutput(TrainDiagram diagram) {
        return new XmlStartPositionsOutput(diagram, this.getCharset());
    }

    @Override
    public EndPositionsOutput createEndPositionsOutput(TrainDiagram diagram) {
        return new XmlEndPositionsOutput(diagram, this.getCharset());
    }

    @Override
    public StationTimetablesOutput creStationTimetablesOutput(TrainDiagram diagram) {
        return new XmlStationTimetablesOutput(diagram, this.getCharset());
    }

    private Charset getCharset() {
        Charset charset = (Charset) this.getParameter("charset");
        if (charset == null) {
            charset = Charset.forName("utf-8");
        }
        return charset;
    }
}
