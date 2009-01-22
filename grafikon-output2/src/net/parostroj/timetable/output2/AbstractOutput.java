package net.parostroj.timetable.output2;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Abstract output.
 *
 * @author jub
 */
public class AbstractOutput implements Output {

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }

}
