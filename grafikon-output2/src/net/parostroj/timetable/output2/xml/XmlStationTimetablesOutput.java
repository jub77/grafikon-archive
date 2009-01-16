package net.parostroj.timetable.output2.xml;

import java.io.IOException;
import java.io.Writer;
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
import net.parostroj.timetable.output2.util.OutputHelper;

/**
 * Xml output for station timetables.
 *
 * @author jub
 */
class XmlStationTimetablesOutput implements StationTimetablesOutput {

    private TrainDiagram diagram;

    public XmlStationTimetablesOutput(TrainDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void writeTo(Writer writer) throws IOException {
        try {
            // extract positions
            StationTimetablesExtractor se = new StationTimetablesExtractor(diagram, this.getNodes());
            StationTimetables st = new StationTimetables(se.getStationTimetables());

            JAXBContext context = JAXBContext.newInstance(StationTimetables.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, OutputHelper.getEncoding(writer, "utf-8"));
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
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
