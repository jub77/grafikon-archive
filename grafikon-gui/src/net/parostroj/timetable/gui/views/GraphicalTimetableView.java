/*
 * TrainGraphicalTimetableView.java
 *
 * Created on 24. srpen 2007, 13:41
 */

package net.parostroj.timetable.gui.views;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.parostroj.timetable.gui.*;
import net.parostroj.timetable.gui.dialogs.EditRoutesDialog;
import net.parostroj.timetable.gui.dialogs.SaveGTDialog;
import net.parostroj.timetable.gui.views.GraphicalTimetableView.TrainColors;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.ResourceLoader;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * Graphical timetable view.
 *
 * @author jub
 */
public class GraphicalTimetableView extends javax.swing.JPanel implements ApplicationModelListener,ChangeListener {
    
    private static final Logger LOG = Logger.getLogger(GraphicalTimetableView.class.getName());

    private GTDraw draw;
    
    private TrainRegionCollector trainRegionCollector;
    
    private TrainSelector trainSelector;
    
    private ApplicationModel model;
    
    private int shift;
    
    private HighlightedTrains hTrains;
    
    private Type type = Type.CLASSIC;
    
    private enum Type {
        CLASSIC, WITH_TRACKS;
    }
    
    public enum TrainColors {
        BY_TYPE, BY_COLOR_CHOOSER;
    }
    
    private final static int MIN_WIDTH = 1000;
    
    private final static int MAX_WIDTH = 10000;
    
    private final static int WIDTH_STEPS = 10;
    
    private final static int INITIAL_WIDTH = 4;
    
    private final static int SELECTION_RADIUS = 7;
    
    private int currentSize;
    
    private TrainColors trainColors = TrainColors.BY_TYPE;
    
    private TrainColorChooser trainColorChooser;
    
    private SaveGTDialog dialog;
    
    private EditRoutesDialog editRoutesDialog;

    /** Creates new form TrainGraphicalTimetableView */
    public GraphicalTimetableView() {
        initComponents();
        
        this.setGTWidth(INITIAL_WIDTH);
        this.setBackground(Color.WHITE);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setRoute(getRoute());
            }
        });
        
        dialog = new SaveGTDialog(null,true);
        
        editRoutesDialog = new EditRoutesDialog(null, true);
        
        this.addSizesToMenu();
    }
    
    private void setGTWidth(int size) {
        int newWidth = MIN_WIDTH + (size - 1) * ((MAX_WIDTH - MIN_WIDTH) / (WIDTH_STEPS - 1));
        Dimension d = this.getPreferredSize();
        d.width = newWidth;
        this.setPreferredSize(d);
        currentSize = size;
        this.revalidate();
    }

    public TrainColors getTrainColors() {
        return trainColors;
    }

    public void setTrainColors(TrainColors trainColors, TrainColorChooser chooser) {
        this.trainColors = trainColors;
        this.trainColorChooser = chooser;
    }

    public void setTrainSelector(TrainSelector trainSelector) {
        this.trainSelector = trainSelector;
    }
    
    public void setModel(ApplicationModel model) {
        this.model = model;
        model.addListener(this);
        editRoutesDialog.setModel(model);
    }

    public void setRoute(Route route) {
        if (route == null)
            draw = null;
        else {
            Dimension d = this.getSize();
            trainRegionCollector = new TrainRegionCollector(SELECTION_RADIUS);
            if (type == Type.CLASSIC) {
                draw = new GTDrawClassic(new Point(10,20), 100, this.getSize(), route, trainColors, trainColorChooser,hTrains,trainRegionCollector);
            } else if (type == Type.WITH_TRACKS) {
                draw = new GTDrawWithNodeTracks(new Point(10,20), 100, this.getSize(), route, trainColors, trainColorChooser,hTrains,trainRegionCollector);
            }
            // set preferences
            this.setPreferencesToDraw(draw);
            
            this.setShiftX(shift);
        }
        this.repaint();
    }
    
    private void setPreferencesToDraw(GTDraw gtDraw) {
        if (gtDraw != null) {
            if (addigitsCheckBoxMenuItem.isSelected())
                gtDraw.setPreference(GTDrawPreference.ARRIVAL_DEPARTURE_DIGITS, true);
            if (extendedLinesCheckBoxMenuItem.isSelected())
                gtDraw.setPreference(GTDrawPreference.EXTENDED_LINES, true);
            if (trainNamesCheckBoxMenuItem.isSelected())
                gtDraw.setPreference(GTDrawPreference.TRAIN_NAMES, true);
        }
    }
    
    private Route getRoute() {
        if (draw == null)
            return null;
        else
            return draw.getRoute();
    }
    
    private void setShiftX(int shift) {
        if (draw != null)
            draw.setPositionX(shift);
        this.shift = shift;
    }
    
    private void setType(Type type) {
        this.type = type;
        this.setRoute(this.getRoute());
        this.repaint();
    }

    public void setHTrains(HighlightedTrains hTrains) {
        this.hTrains = hTrains;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        routesMenu = new javax.swing.JMenu();
        routesEditMenuItem = new javax.swing.JMenuItem();
        javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JMenu typesMenu = new javax.swing.JMenu();
        classicMenuItem = new javax.swing.JRadioButtonMenuItem();
        withTracksMenuItem = new javax.swing.JRadioButtonMenuItem();
        sizesMenu = new javax.swing.JMenu();
        preferencesMenu = new javax.swing.JMenu();
        addigitsCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        extendedLinesCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        trainNamesCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        javax.swing.JSeparator jSeparator2 = new javax.swing.JSeparator();
        saveMenuItem = new javax.swing.JMenuItem();
        typesButtonGroup = new javax.swing.ButtonGroup();
        routesGroup = new javax.swing.ButtonGroup();

        routesMenu.setText(ResourceLoader.getString("gt.routes")); // NOI18N
        popupMenu.add(routesMenu);

        routesEditMenuItem.setText(ResourceLoader.getString("gt.routes.edit")); // NOI18N
        routesEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routesEditMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(routesEditMenuItem);
        popupMenu.add(jSeparator1);

        typesMenu.setText(ResourceLoader.getString("gt.type")); // NOI18N

        typesButtonGroup.add(classicMenuItem);
        classicMenuItem.setSelected(true);
        classicMenuItem.setText(ResourceLoader.getString("gt.classic")); // NOI18N
        classicMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classicMenuItemActionPerformed(evt);
            }
        });
        typesMenu.add(classicMenuItem);

        typesButtonGroup.add(withTracksMenuItem);
        withTracksMenuItem.setText(ResourceLoader.getString("gt.withtracks")); // NOI18N
        withTracksMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withTracksMenuItemActionPerformed(evt);
            }
        });
        typesMenu.add(withTracksMenuItem);

        popupMenu.add(typesMenu);

        sizesMenu.setText(ResourceLoader.getString("gt.sizes")); // NOI18N
        popupMenu.add(sizesMenu);

        preferencesMenu.setText(ResourceLoader.getString("gt.preferences")); // NOI18N

        addigitsCheckBoxMenuItem.setText(ResourceLoader.getString("gt.addigits")); // NOI18N
        addigitsCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferencesCheckBoxMenuItemActionPerformed(evt);
            }
        });
        preferencesMenu.add(addigitsCheckBoxMenuItem);

        extendedLinesCheckBoxMenuItem.setText(ResourceLoader.getString("gt.extendedlines")); // NOI18N
        extendedLinesCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferencesCheckBoxMenuItemActionPerformed(evt);
            }
        });
        preferencesMenu.add(extendedLinesCheckBoxMenuItem);

        trainNamesCheckBoxMenuItem.setSelected(true);
        trainNamesCheckBoxMenuItem.setText(ResourceLoader.getString("gt.trainnames")); // NOI18N
        trainNamesCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferencesCheckBoxMenuItemActionPerformed(evt);
            }
        });
        preferencesMenu.add(trainNamesCheckBoxMenuItem);

        popupMenu.add(preferencesMenu);
        popupMenu.add(jSeparator2);

        saveMenuItem.setText(ResourceLoader.getString("gt.save")); // NOI18N
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(saveMenuItem);

        setDoubleBuffered(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
    if (this.getRoute() == null) {
        return;
    }
    dialog.setLocationRelativeTo(this.getParent());
    dialog.setVisible(true);

    if (!dialog.isSave()) {
        return;
    }
    // get values and provide save
    GTDraw drawFile = null;
    if (type == Type.CLASSIC) {
        drawFile = new GTDrawClassic(new Point(10, 20), 100, dialog.getSaveSize(), this.getRoute(), trainColors, trainColorChooser,null,null);
    } else if (type == Type.WITH_TRACKS) {
        drawFile = new GTDrawWithNodeTracks(new Point(10, 20), 100, dialog.getSaveSize(), this.getRoute(), trainColors, trainColorChooser,null,null);
    }
    this.setPreferencesToDraw(drawFile);

    if (dialog.getType() == SaveGTDialog.Type.PNG) {
        BufferedImage img = new BufferedImage(dialog.getSaveSize().width, dialog.getSaveSize().height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, dialog.getSaveSize().width, dialog.getSaveSize().height);
        drawFile.draw(g2d);

        try {
            ImageIO.write(img, "png", dialog.getSaveFile());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, ResourceLoader.getString("save.image.error"), ResourceLoader.getString("save.image.error.text"), JOptionPane.ERROR_MESSAGE);
        }
    } else if (dialog.getType() == SaveGTDialog.Type.SVG) {
        DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        SVGGeneratorContext context = SVGGeneratorContext.createDefault(document);
        SVGGraphics2D g2d = new SVGGraphics2D(context,false);

        g2d.setSVGCanvasSize(dialog.getSaveSize());

        drawFile.draw(g2d);

        // write to ouput - do not use css style
        boolean useCSS = false;
        try {
            Writer out = new OutputStreamWriter(new FileOutputStream(dialog.getSaveFile()), "UTF-8");
            g2d.stream(out, useCSS);
        } catch (IOException e) {
            // do nothing for this moment
        }
    }
}//GEN-LAST:event_saveMenuItemActionPerformed

private void withTracksMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withTracksMenuItemActionPerformed
    this.setType(Type.WITH_TRACKS);
}//GEN-LAST:event_withTracksMenuItemActionPerformed

private void classicMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classicMenuItemActionPerformed
    this.setType(Type.CLASSIC);
}//GEN-LAST:event_classicMenuItemActionPerformed

private void routesEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routesEditMenuItemActionPerformed
    if (model.getDiagram() == null)
        return;
    
    editRoutesDialog.updateValues();
    
    editRoutesDialog.setLocationRelativeTo(this.getParent());
    editRoutesDialog.setVisible(true);
}//GEN-LAST:event_routesEditMenuItemActionPerformed

private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
    // selection of the train
    if (trainRegionCollector != null) {
        List<Train> selectedTrains = trainRegionCollector.getTrainForPoint(evt.getX(), evt.getY());
        if (trainSelector != null) {
            if (selectedTrains.size() == 0)
                trainSelector.selectTrain(null);
            else {
                Train oldSelection = trainSelector.getSelectedTrain();
                if (oldSelection == null)
                    trainSelector.selectTrain(selectedTrains.get(0));
                else {
                    int oldIndex = selectedTrains.indexOf(oldSelection);
                    if (oldIndex == -1)
                        trainSelector.selectTrain(selectedTrains.get(0));
                    else {
                        oldIndex += 1;
                        if (oldIndex >= selectedTrains.size())
                            oldIndex = 0;
                        trainSelector.selectTrain(selectedTrains.get(oldIndex));
                    }
                }
            }
        }
    }
}//GEN-LAST:event_formMouseClicked

private void preferencesCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preferencesCheckBoxMenuItemActionPerformed
    // update GTDraw using setRoute with the same route
    this.setRoute(this.getRoute());
}//GEN-LAST:event_preferencesCheckBoxMenuItemActionPerformed

    @Override
    public void paint(Graphics g) {
        LOG.finest("Starting paint.");
        super.paint(g);
        
        if (draw != null)
            draw.draw((Graphics2D)g);
        else {
            // draw information about context menu
            g.drawString(ResourceLoader.getString("gt.contextmenu.info"), 20, 20);
        }
        LOG.finest("Finished paint.");
    }
    
    @Override
    public void modelChanged(ApplicationModelEvent event) {
        ApplicationModel lModel = event.getModel();
        TrainDiagram diagram = lModel.getDiagram();
        switch (event.getType()) {
            case SET_DIAGRAM_CHANGED:
                if (diagram == null) {
                    this.setRoute(null);
                    this.setComponentPopupMenu(null);
                } else {
                    this.setComponentPopupMenu(popupMenu);
                    if (diagram.getRoutes().size() > 0) {
                        this.setRoute(lModel.getDiagram().getRoutes().get(0));
                    } else
                        this.setRoute(null);
                    this.createMenuForRoutes(diagram.getRoutes());
                }
                this.repaint();
                break;
            case ROUTES_MODIFIED:
                // changed list of routes
                this.createMenuForRoutes(diagram.getRoutes());
                // check current route
                if (this.getRoute() == (Route)event.getObject()) {
                    if (!diagram.getRoutes().contains(this.getRoute())) {
                        this.setRoute(null);
                    }
                    this.repaint();
                }
                break;
            case DELETE_TRAIN:
                if (trainRegionCollector != null)
                    trainRegionCollector.deleteTrain((Train)event.getObject());
                this.repaint();
                break;
            case MODIFIED_TRAIN:
                if (trainRegionCollector != null)
                    trainRegionCollector.modifiedTrain((Train)event.getObject());
                this.repaint();
                break;
            case MODIFIED_TRAIN_NAME_TYPE:
                this.repaint();
                break;
            case NEW_TRAIN:
                if (trainRegionCollector != null)
                    trainRegionCollector.newTrain((Train)event.getObject());
                this.repaint();
                break;
            case TRAIN_TYPES_CHANGED:
                // handling change of the color of the train type
                this.repaint();
                break;
        }
    }
    
    private void createMenuForRoutes(List<Route> routes) {
        routesGroup = new ButtonGroup();
        routesMenu.removeAll();
        int cnt = 0;
        for (Route lRoute : routes) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(lRoute.toString());
            item.setActionCommand(Integer.toString(cnt++));
            routesMenu.add(item);
            routesGroup.add(item);
            if (lRoute == this.getRoute())
                item.setSelected(true);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    activateRoute(model.getDiagram().getRoutes().get(Integer.parseInt(e.getActionCommand())));
                }
            });
        }
    }
    
    private void addSizesToMenu() {
        ButtonGroup group = new ButtonGroup();
        for (int i = 1; i <= WIDTH_STEPS; i++) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(Integer.toString(i));
            item.setActionCommand(Integer.toString(i));
            if (i == currentSize)
                item.setSelected(true);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setGTSize(Integer.parseInt(e.getActionCommand()));
                }
            });
            group.add(item);
            sizesMenu.add(item);
        }
    
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int oldShift = this.shift;
        if (e.getSource() instanceof JViewport) {
            int newShift = ((JViewport)e.getSource()).getViewPosition().x;
            if (newShift != oldShift) {
                this.setShiftX(newShift);
                this.repaint(oldShift + 10,0,100,this.getHeight());
                this.repaint(newShift + 10,0,100,this.getHeight());
            }
        }
    }
    
    private void activateRoute(Route route) {
        this.setRoute(route);
        this.repaint();
    }
    
    private void setGTSize(int size) {
        this.setGTWidth(size);
        this.setRoute(this.getRoute());
        this.repaint();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem addigitsCheckBoxMenuItem;
    private javax.swing.JRadioButtonMenuItem classicMenuItem;
    private javax.swing.JCheckBoxMenuItem extendedLinesCheckBoxMenuItem;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JMenu preferencesMenu;
    private javax.swing.JMenuItem routesEditMenuItem;
    private javax.swing.ButtonGroup routesGroup;
    private javax.swing.JMenu routesMenu;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu sizesMenu;
    private javax.swing.JCheckBoxMenuItem trainNamesCheckBoxMenuItem;
    private javax.swing.ButtonGroup typesButtonGroup;
    private javax.swing.JRadioButtonMenuItem withTracksMenuItem;
    // End of variables declaration//GEN-END:variables
}
