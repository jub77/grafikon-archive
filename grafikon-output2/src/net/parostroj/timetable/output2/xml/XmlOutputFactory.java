package net.parostroj.timetable.output2.xml;

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
        return new XmlStartPositionsOutput(diagram);
    }

    @Override
    public EndPositionsOutput createEndPositionsOutput(TrainDiagram diagram) {
        return new XmlEndPositionsOutput(diagram);
    }

    @Override
    public StationTimetablesOutput creStationTimetablesOutput(TrainDiagram diagram) {
        return new XmlStationTimetablesOutput(diagram);
    }
}
