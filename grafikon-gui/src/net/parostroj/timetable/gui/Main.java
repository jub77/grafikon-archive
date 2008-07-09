package net.parostroj.timetable.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
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
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        ApplicationStarter starter = new ApplicationStarter(MainFrame.class, 310, 110, Main.class.getResource("/images/splashscreen.png"));
        starter.start();
    }
}
