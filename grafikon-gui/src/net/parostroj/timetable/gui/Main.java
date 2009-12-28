package net.parostroj.timetable.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.RepaintManager;
import javax.swing.UIManager;
import net.parostroj.timetable.gui.utils.CheckThreadViolationRepaintManager;

/**
 * Class with main method.
 * 
 * @author jub
 */
public class Main {
    static {
        Logger.getLogger("net.parostroj").setLevel(Level.FINE);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.ALL);
        
        // add file output to logging
        try {
            File file = new File(System.getProperty("java.io.tmpdir"), "grafikon.log");
            Handler handler = new FileHandler(file.getCanonicalPath());
            handler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(handler);
        } catch (IOException e) {
            Logger.getLogger("net.parostroj").log(Level.WARNING, "Cannot initialize logging file.", e);
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        if (Boolean.TRUE.equals(AppPreferences.getPreferences().getBoolean("debug")))
            setDebug();
        ApplicationStarter starter = new ApplicationStarter(MainFrame.class, 296, 112, Main.class.getResource("/images/splashscreen.png"));
        starter.start();
    }

    private static void setDebug() {
        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager(false));
        Logger.getLogger("net.parostroj").setLevel(Level.ALL);
    }
}
