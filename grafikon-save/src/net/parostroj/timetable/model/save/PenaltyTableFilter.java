package net.parostroj.timetable.model.save;

import net.parostroj.timetable.model.TrainDiagram;
import net.parostroj.timetable.model.ls.LSException;
import net.parostroj.timetable.model.ls.ModelVersion;

/**
 * Penalty table filter.
 *
 * @author jub
 */
public class PenaltyTableFilter implements TrainDiagramFilter {

    @Override
    public TrainDiagram filter(TrainDiagram diagram, ModelVersion version) throws LSException {
        LSPenaltyTableHelper.fillPenaltyTable(diagram.getPenaltyTable());
        return diagram;
    }

}
