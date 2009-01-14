package net.parostroj.timetable.output2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Interface for starting positions output.
 *
 * @author jub
 */
public interface StartPositionsOutput {
    /**
     * writes starting positions into a writer.
     *
     * @param writer
     * @throws IOException
     */
    public void writeTo(Writer writer) throws IOException;

    /**
     * writes starting positions into a stream.
     *
     * @param stream
     * @throws IOException
     */
    public void writeTo(OutputStream stream) throws IOException;
}
