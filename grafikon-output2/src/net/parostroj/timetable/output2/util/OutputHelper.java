package net.parostroj.timetable.output2.util;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * Output helper. It deals with streams and writers/readers.
 *
 * @author jub
 */
public class OutputHelper {

    public static String getEncoding(Writer writer, String defaultEncoding) {
        String encoding = defaultEncoding;
        if (writer instanceof OutputStreamWriter) {
            OutputStreamWriter w = (OutputStreamWriter) writer;
            encoding = w.getEncoding();
        }
        Charset ch = Charset.forName(encoding);
        return ch.name();
    }
}
