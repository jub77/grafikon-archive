package net.parostroj.timetable.output;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract superclass for templates.
 * 
 * @author jub
 */
abstract public class Templates {
    
    private static final Logger LOG = Logger.getLogger(Templates.class.getName());
    
    private static Locale locale;
    
    public static void setLocale(Locale newLocale) {
        locale = newLocale;
    }
    
    public static Locale getLocale() {
        return locale;
    }
    
    public static Locale getLocaleForTemplate() {
        return locale != null ? locale : Locale.getDefault();
    }
    
    public static Locale parseLocale(String localeString) {
        Locale returnedLocale = null;
        if (localeString != null) {
            String parts[] = localeString.split("_");
            if (parts.length == 1) {
                returnedLocale = new Locale(parts[0]);
            } else if (parts.length == 2) {
                returnedLocale = new Locale(parts[0],parts[1]);
            } else if (parts.length == 3) {
                returnedLocale = new Locale(parts[0], parts[1], parts[2]);
            }
        }
        return returnedLocale;
    }

    protected static String readTextFile(String filename) {
        try {
            InputStream fis = TrainTimetablesListTemplates.class.getResourceAsStream(filename);
            Reader in = new InputStreamReader(fis, "utf-8");
            char[] buffer = new char[1000];
            int read = 0;
            StringBuilder result = new StringBuilder();
            do {
                read = in.read(buffer);
                if (read != -1) {
                    result.append(buffer, 0, read);
                }
            } while (read != -1);
            in.close();
            return result.toString();
        } catch (FileNotFoundException e) {
            LOG.log(Level.WARNING, e.getMessage(),e);
            return "";
        } catch (IOException e) {
            LOG.log(Level.WARNING, e.getMessage(),e);
            return "";
        }
    }
}
