package net.parostroj.timetable.output2.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.output2.AbstractOutput;
import net.parostroj.timetable.output2.OutputException;
import net.parostroj.timetable.output2.OutputParams;
import net.parostroj.timetable.output2.impl.Position;
import net.parostroj.timetable.output2.impl.PositionsExtractor;
import net.parostroj.timetable.output2.util.ResourceHelper;
import org.mvel2.templates.TemplateRuntime;

/**
 * Starting positions output to html.
 *
 * @author jub
 */
public class HtmlStartPositionsOutput extends AbstractOutput {

    private Locale locale;

    HtmlStartPositionsOutput(Locale locale) {
        this.locale = locale;
    }

    private void writeTo(OutputStream stream, TrainDiagram diagram) throws IOException {
        // extract positions
        PositionsExtractor pe = new PositionsExtractor(diagram);
        List<Position> engines = pe.getStartPositionsEngines();
        List<Position> trainUnits = pe.getStartPositionsTrainUnits();

        // call template
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("engines", engines);
        map.put("train_units", trainUnits);
        ResourceHelper.addTextsToMap(map, "start_positions_", locale, "texts/html_texts");

        String template = ResourceHelper.readResource("/templates/start_positions.html");
        String ret = (String) TemplateRuntime.eval(template, map);

        Writer writer = new OutputStreamWriter(stream, "utf-8");

        writer.write(ret);
        writer.flush();
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
