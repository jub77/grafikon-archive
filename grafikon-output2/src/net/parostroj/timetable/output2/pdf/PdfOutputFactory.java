package net.parostroj.timetable.output2.pdf;

import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.Output;
import net.parostroj.timetable.output2.OutputFactory;

/**
 * Pdf output factory. Uses xsl-fo for creating the output.
 *
 * @author jub
 */
public class PdfOutputFactory extends OutputFactory {

    public PdfOutputFactory() {
    }

    @Override
    public Output createOutput(String type, TrainDiagram diagram) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
