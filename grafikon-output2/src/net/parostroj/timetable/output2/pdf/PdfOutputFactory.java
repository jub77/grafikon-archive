package net.parostroj.timetable.output2.pdf;

import java.util.Locale;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.EndPositionsOutput;
import net.parostroj.timetable.output2.OutputFactory;
import net.parostroj.timetable.output2.StartPositionsOutput;
import net.parostroj.timetable.output2.StationTimetablesOutput;

/**
 * Pdf output factory. Uses xsl-fo for creating the output.
 *
 * @author jub
 */
public class PdfOutputFactory extends OutputFactory {

    public PdfOutputFactory() {
    }

    @Override
    public StartPositionsOutput createStartPositionsOutput(TrainDiagram diagram) {
        return new PdfStartPositionsOutput(diagram);
    }

    @Override
    public EndPositionsOutput createEndPositionsOutput(TrainDiagram diagram) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StationTimetablesOutput creStationTimetablesOutput(TrainDiagram diagram) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
