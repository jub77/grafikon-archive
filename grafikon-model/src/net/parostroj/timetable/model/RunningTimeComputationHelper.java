package net.parostroj.timetable.model;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * This class holds usefull information for computation of running time.
 *
 * @author jub
 */
public class RunningTimeComputationHelper {

    private static final Logger LOG = Logger.getLogger(RunningTimeComputationHelper.class.getName());
    /** Filename of the file with information. */
    private static final String SPEEDING_BRAKING_PENALTIES_FILENAME = "/speeding_braking_penalties.xml";
    private static PenaltyTable penaltyTable;
    

    static {
        initialization();
    }

    /**
     * initializes information.
     */
    private static void initialization() {
        try {
            JAXBContext context = JAXBContext.newInstance(PenaltyTable.class);
            Unmarshaller u = context.createUnmarshaller();
            URL url = RunningTimeComputationHelper.class.getResource(SPEEDING_BRAKING_PENALTIES_FILENAME);
            if (url == null) {
                throw new RuntimeException("Cannot find speeding braking table to load.");
            }
            penaltyTable = u.unmarshal(new javax.xml.transform.stream.StreamSource(url.openStream()), net.parostroj.timetable.model.PenaltyTable.class).getValue();
            LOG.fine("Penalty table loaded.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error loading penalty table.", e);
        }
    }

    /**
     * @return penalty table
     */
    public static PenaltyTable getPenaltyTable() {
        return penaltyTable;
    }
}