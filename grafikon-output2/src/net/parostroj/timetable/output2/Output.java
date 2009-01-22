package net.parostroj.timetable.output2;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Output.
 *
 * @author jub
 */
public interface Output {
    /**
     * writes output to stream.
     *
     * @param stream
     * @throws java.io.IOException
     */
    public void writeTo(OutputStream stream) throws IOException;
}
