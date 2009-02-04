package net.parostroj.timetable.output2.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.AbstractOutput;
import net.parostroj.timetable.output2.OutputException;
import net.parostroj.timetable.output2.OutputParams;
import net.parostroj.timetable.output2.impl.Position;
import net.parostroj.timetable.output2.impl.PositionsExtractor;

/**
 * Xml export of end positions.
 *
 * @author jub
 */
class XmlEndPositionsOutput extends AbstractOutput {

    private Charset charset;

    public XmlEndPositionsOutput(Charset charset) {
        this.charset = charset;
    }

    private void writeTo(OutputStream stream, TrainDiagram diagram) throws IOException {
        try {
            // extract positions
            PositionsExtractor pe = new PositionsExtractor(diagram);
            List<Position> engines = pe.getEndPositionsEngines();
            List<Position> trainUnits = pe.getEndPositionsTrainUnits();

            EndPositions ep = new EndPositions();
            ep.setEnginesPositions(engines);
            ep.setTrainUnitsPositions(trainUnits);

            JAXBContext context = JAXBContext.newInstance(EndPositions.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, charset.name());
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            Writer writer = new OutputStreamWriter(stream, charset);

            m.marshal(ep, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(OutputParams params) throws OutputException {
        TrainDiagram diagram = (TrainDiagram) params.getParam("diagram").getValue();
        OutputStream stream = (OutputStream) params.getParam("output.stream").getValue();
        try {
            this.writeTo(stream, diagram);
        } catch (IOException e) {
            throw new OutputException(e);
        }
    }
}
