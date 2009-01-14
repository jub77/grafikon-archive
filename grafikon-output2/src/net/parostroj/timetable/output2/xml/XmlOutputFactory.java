package net.parostroj.timetable.output2.xml;

import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.OutputFactory;
import net.parostroj.timetable.output2.StartPositionsOutput;

/**
 * Xml output factory.
 *
 * @author jub
 */
public class XmlOutputFactory extends OutputFactory {

    @Override
    public StartPositionsOutput createStartPositionsOutput(TrainDiagram diagram) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
