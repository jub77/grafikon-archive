package net.parostroj.timetable.output2;

import java.io.IOException;
import java.io.Writer;

/**
 * Interface for end positions output.
 *
 * @author jub
 */
public interface EndPositionsOutput {
    /**
     * writes end positions into a writer.
     *
     * @param writer
     * @throws IOException
     */
    public void writeTo(Writer writer) throws IOException;
}
