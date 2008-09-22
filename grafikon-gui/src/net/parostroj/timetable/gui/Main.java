package net.parostroj.timetable.gui;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.UIManager;

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
            Handler handler = new FileHandler("grafikon.log");
            handler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(handler);
        } catch (IOException e) {
            Logger.getLogger("net.parostroj").log(Level.WARNING, "Cannot initialize logging file.", e);
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        ApplicationStarter starter = new ApplicationStarter(MainFrame.class, 310, 110, Main.class.getResource("/images/splashscreen.png"));
        starter.start();
    }
}
