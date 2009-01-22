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
import net.parostroj.timetable.actions.NodeFilter;
import net.parostroj.timetable.actions.NodeSort;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.StationTimetablesOutput;
import net.parostroj.timetable.output2.impl.StationTimetablesExtractor;

/**
 * Xml output for station timetables.
 *
 * @author jub
 */
class XmlStationTimetablesOutput implements StationTimetablesOutput {

    private TrainDiagram diagram;
    private Charset charset;

    public XmlStationTimetablesOutput(TrainDiagram diagram, Charset charset) {
        this.diagram = diagram;
        this.charset = charset;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        try {
            // extract positions
            StationTimetablesExtractor se = new StationTimetablesExtractor(diagram, this.getNodes());
            StationTimetables st = new StationTimetables(se.getStationTimetables());

            JAXBContext context = JAXBContext.newInstance(StationTimetables.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, charset.name());
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            Writer writer = new OutputStreamWriter(stream, charset);
            m.marshal(st, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    private List<Node> getNodes() {
        NodeSort s = new NodeSort(NodeSort.Type.ASC);
        return s.sort(diagram.getNet().getNodes(), new NodeFilter() {

            @Override
            public boolean check(Node node) {
                return node.getType().isStation() || node.getType().isStop();
            }
        });
    }
}
