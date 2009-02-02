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
import net.parostroj.timetable.output2.Output;
import net.parostroj.timetable.output2.impl.Position;
import net.parostroj.timetable.output2.impl.PositionsExtractor;

/**
 * Xml export of start positions.
 *
 * @author jub
 */
class XmlStartPositionsOutput implements Output {

    private TrainDiagram diagram;
    private Charset charset;

    public XmlStartPositionsOutput(TrainDiagram diagram, Charset charset) {
        this.diagram = diagram;
        this.charset = charset;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        try {
            // extract positions
            PositionsExtractor pe = new PositionsExtractor(diagram);
            List<Position> engines = pe.getStartPositionsEngines();
            List<Position> trainUnits = pe.getStartPositionsTrainUnits();

            StartPositions sp = new StartPositions();
            sp.setEnginesPositions(engines);
            sp.setTrainUnitsPositions(trainUnits);

            JAXBContext context = JAXBContext.newInstance(StartPositions.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, charset.name());
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            Writer writer = new OutputStreamWriter(stream, charset);
            m.marshal(sp, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }
}
