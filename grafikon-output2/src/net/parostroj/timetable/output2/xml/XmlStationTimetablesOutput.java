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
import net.parostroj.timetable.output2.AbstractOutput;
import net.parostroj.timetable.output2.OutputException;
import net.parostroj.timetable.output2.OutputParams;
import net.parostroj.timetable.output2.impl.StationTimetablesExtractor;

/**
 * Xml output for station timetables.
 *
 * @author jub
 */
class XmlStationTimetablesOutput extends AbstractOutput {

    private Charset charset;

    public XmlStationTimetablesOutput(Charset charset) {
        this.charset = charset;
    }

    private void writeTo(OutputStream stream, TrainDiagram diagram) throws IOException {
        try {
            // extract positions
            StationTimetablesExtractor se = new StationTimetablesExtractor(diagram, this.getNodes(diagram));
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

    private List<Node> getNodes(TrainDiagram diagram) {
        NodeSort s = new NodeSort(NodeSort.Type.ASC);
        return s.sort(diagram.getNet().getNodes(), new NodeFilter() {

            @Override
            public boolean check(Node node) {
                return node.getType().isStation() || node.getType().isStop();
            }
        });
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
