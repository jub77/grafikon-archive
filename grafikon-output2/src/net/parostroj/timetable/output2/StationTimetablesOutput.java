package net.parostroj.timetable.output2;

import java.io.IOException;
import java.io.Writer;

/**
 * Interface for station timetables output.
 *
 * @author jub
 */
public interface StationTimetablesOutput {
    /**
     * writes station timetables into a writer.
     *
     * @param writer
     * @throws IOException
     */
    public void writeTo(Writer writer) throws IOException;
}
