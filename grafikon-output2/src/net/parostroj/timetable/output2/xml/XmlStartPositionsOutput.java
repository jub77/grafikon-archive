package net.parostroj.timetable.output2.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.StartPositionsOutput;
import net.parostroj.timetable.output2.impl.Position;
import net.parostroj.timetable.output2.impl.PositionsExtractor;
import net.parostroj.timetable.output2.util.OutputHelper;

/**
 * Xml export of start positions.
 *
 * @author jub
 */
class XmlStartPositionsOutput implements StartPositionsOutput {

    private TrainDiagram diagram;

    public XmlStartPositionsOutput(TrainDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void writeTo(Writer writer) throws IOException {
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
            m.setProperty(Marshaller.JAXB_ENCODING, OutputHelper.getEncoding(writer, "utf-8"));
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(sp, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }
}
