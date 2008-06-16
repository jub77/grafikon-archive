package net.parostroj.timetable.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import net.parostroj.timetable.model.ls.LSFileFactory;
import net.parostroj.timetable.model.ls.ModelVersion;
import net.parostroj.timetable.model.ls.impl3.FileLoadSaveImpl;
import net.parostroj.timetable.model.save.LoadSave;

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
        // register load/save
        LSFileFactory factory = LSFileFactory.getInstance();
        factory.registerLS(new ModelVersion(1, 0), LoadSave.class);
        factory.registerLS(new ModelVersion(2, 0), LoadSave.class);
        factory.registerLS(new ModelVersion(3,0), FileLoadSaveImpl.class);

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        ApplicationStarter starter = new ApplicationStarter(MainFrame.class, 310, 110, Main.class.getResource("/images/splashscreen.png"));
        starter.start();
    }
}
