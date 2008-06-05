/*
 * SettingsDialog.java
 *
 * Created on 22. září 2007, 18:07
 */
package net.parostroj.timetable.gui.dialogs;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.parostroj.timetable.gui.*;
import net.parostroj.timetable.model.*;
import net.parostroj.timetable.utils.ResourceLoader;

/**
 * Dialog for settings modification of the train diagram.
 *
 * @author jub
 */
public class SettingsDialog extends javax.swing.JDialog implements ApplicationModelListener {
    
    private static final Logger LOG = Logger.getLogger(SettingsDialog.class.getName());
    
    private ApplicationModel model;
    
    /** Creates new form SettingsDialog */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        sortComboBox.addItem(ResourceLoader.getString("modelinfo.sort.number"));
        sortComboBox.addItem(ResourceLoader.getString("modelinfo.sort.string"));
        sortComboBox.setPrototypeDisplayValue("nnnnnnnnnnnnn");
        
        completeNameTemplateTextField.setColumns(50);
        
        pack();
    }
    
    public void setModel(ApplicationModel model) {
        this.model = model;
        this.model.addListener(this);
        
        for (Scale scale : Scale.values()) {
            scaleComboBox.addItem(scale);
        }
        
        // set some values for speed
        for (double d = 4.0; d <= 6.0 ;) {
            ratioComboBox.addItem(Double.toString(d));
            d += 0.5;
        }
        
        this.updateValues();
    }
    
    private void updateValues() {
        if (model.getDiagram() != null) {
            // set original values ...
            scaleComboBox.setSelectedItem(model.getDiagram().getAttribute("scale"));
            ratioComboBox.setSelectedItem(((Double)model.getDiagram().getAttribute("time.scale")).toString());
            
            // sorting
            TrainsData trainsData = model.getDiagram().getTrainsData();
            SortPatternGroup firstGroup = trainsData.getTrainSortPattern().getGroups().get(0);
            if (firstGroup.getType() == SortPatternGroup.Type.NUMBER) {
                sortComboBox.setSelectedIndex(0);
            } else {
                sortComboBox.setSelectedIndex(1);
            }
            completeNameTemplateTextField.setText(trainsData.getTrainCompleteNameTemplate());
            completeNameTemplateTextField.setCaretPosition(0);
            nameTemplateTextField.setText(trainsData.getTrainNameTemplate());
            nameTemplateTextField.setCaretPosition(0);
        }
    }

    @Override
    public void modelChanged(ApplicationModelEvent event) {
        switch(event.getType()) {
            case SET_DIAGRAM_CHANGED:
                this.updateValues();
                break;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        scaleComboBox = new javax.swing.JComboBox();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        ratioComboBox = new javax.swing.JComboBox();
        panel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        nameTemplateTextField = new javax.swing.JTextField();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        completeNameTemplateTextField = new javax.swing.JTextField();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        sortComboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(ResourceLoader.getString("modelinfo")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(ResourceLoader.getString("modelinfo.scales")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 5, 10);
        getContentPane().add(scaleComboBox, gridBagConstraints);

        jLabel2.setText(ResourceLoader.getString("modelinfo.ratio")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        getContentPane().add(jLabel2, gridBagConstraints);

        ratioComboBox.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 10);
        getContentPane().add(ratioComboBox, gridBagConstraints);

        okButton.setText(ResourceLoader.getString("button.ok")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        panel1.add(okButton);

        cancelButton.setText(ResourceLoader.getString("button.cancel")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        panel1.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        getContentPane().add(panel1, gridBagConstraints);

        jLabel3.setText(ResourceLoader.getString("edit.traintypes.nametemplate")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        getContentPane().add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
        getContentPane().add(nameTemplateTextField, gridBagConstraints);

        jLabel4.setText(ResourceLoader.getString("edit.traintypes.completenametemplate")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        getContentPane().add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
        getContentPane().add(completeNameTemplateTextField, gridBagConstraints);

        jLabel5.setText(ResourceLoader.getString("modelinfo.sort")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        getContentPane().add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 10);
        getContentPane().add(sortComboBox, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // get templates' values
        TrainsData trainsData = model.getDiagram().getTrainsData();
        String completeName = completeNameTemplateTextField.getText();
        String name = nameTemplateTextField.getText();
        
        if ("".equals(name)|| "".equals(completeName)) {
            JOptionPane.showMessageDialog(this.getParent(), ResourceLoader.getString("dialog.error.emptytemplates"),
                    ResourceLoader.getString("dialog.error.title"), JOptionPane.ERROR_MESSAGE);
            LOG.log(Level.FINE, "Empty templates.");
            return;
        }
        
        // set scale
        Scale s = (Scale)scaleComboBox.getSelectedItem();
        // set ratio
        double sp = 1.0;
        try {
            sp = Double.parseDouble((String)ratioComboBox.getSelectedItem());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this.getParent(), ResourceLoader.getString("dialog.error.badratio"),
                    ResourceLoader.getString("dialog.error.title"), JOptionPane.ERROR_MESSAGE);
            LOG.log(Level.FINE, "Cannot covert ratio.", ex);
            return;
        }
        if (s != null)
            model.getDiagram().setAttribute("scale", s);
        model.getDiagram().setAttribute("time.scale", sp);
        
        // set templates
        trainsData.setTrainCompleteNameTemplate(completeName);
        trainsData.setTrainNameTemplate(name);
        
        // set sorting
        SortPattern sPattern = null;
        if (sortComboBox.getSelectedIndex() == 0) {
            sPattern = new SortPattern("(\\d*)(.*)");
            sPattern.getGroups().add(new SortPatternGroup(1, SortPatternGroup.Type.NUMBER));
            sPattern.getGroups().add(new SortPatternGroup(2, SortPatternGroup.Type.STRING));
        } else {
            sPattern = new SortPattern("(.*)");
            sPattern.getGroups().add(new SortPatternGroup(1, SortPatternGroup.Type.STRING));
        }
        trainsData.setTrainSortPattern(sPattern);
        
        // update model
        for (Train train : model.getDiagram().getTrains()) {
            train.recalculate(model.getDiagram());
        }
        // clear cached information for train names
        for (Train train : model.getDiagram().getTrains())
            train.clearCachedData();

        this.updateValues();
        model.fireEvent(new ApplicationModelEvent(ApplicationModelEventType.SET_DIAGRAM_CHANGED,model));
        model.setModelChanged(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.updateValues();
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField completeNameTemplateTextField;
    private javax.swing.JTextField nameTemplateTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel panel1;
    private javax.swing.JComboBox ratioComboBox;
    private javax.swing.JComboBox scaleComboBox;
    private javax.swing.JComboBox sortComboBox;
    // End of variables declaration//GEN-END:variables
    
}