package net.parostroj.timetable.output2.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.parostroj.timetable.actions.NodeFilter;
import net.parostroj.timetable.actions.NodeSort;
import net.parostroj.timetable.model.Node;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.StationTimetablesOutput;
import net.parostroj.timetable.output2.impl.StationTimetable;
import net.parostroj.timetable.output2.impl.StationTimetablesExtractor;
import net.parostroj.timetable.output2.util.ResourceHelper;
import org.mvel2.templates.TemplateRuntime;

/**
 * Implements html output for station timetable.
 *
 * @author jub
 */
public class HtmlStationTimetablesOutput implements StationTimetablesOutput {

    private TrainDiagram diagram;
    private Locale locale;

    HtmlStationTimetablesOutput(TrainDiagram diagram, Locale locale) {
        this.locale = locale;
        this.diagram = diagram;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        // extract positions
        StationTimetablesExtractor se = new StationTimetablesExtractor(diagram, this.getNodes());
        List<StationTimetable> timetables = se.getStationTimetables();

        // call template
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stations", timetables);
        ResourceHelper.addTextsToMap(map, "stations_", locale, "texts/html_texts");

        String template = ResourceHelper.readResource("/templates/stations.html");
        String ret = (String) TemplateRuntime.eval(template, map);

        Writer writer = new OutputStreamWriter(stream, "utf-8");

        writer.write(ret);
        writer.flush();
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
