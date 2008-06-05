/*
 * LineClassesDialog.java
 *
 * Created on 2. červen 2008, 6:44
 */
package net.parostroj.timetable.gui.dialogs;

import java.util.UUID;
import javax.swing.AbstractListModel;
import net.parostroj.timetable.gui.ApplicationModel;
import net.parostroj.timetable.gui.ApplicationModelEvent;
import net.parostroj.timetable.gui.ApplicationModelEventType;
import net.parostroj.timetable.model.LineClass;
import net.parostroj.timetable.utils.ResourceLoader;

/**
 * Dialog for editing classes of the lines.
 * 
 * @author jub
 */
public class LineClassesDialog extends javax.swing.JDialog {

    private ApplicationModel model;
    private LineClassesListModel listModel;

    private class LineClassesListModel extends AbstractListModel {

        @Override
        public int getSize() {
            if (model.getDiagram() == null) {
                return 0;
            } else {
                return model.getDiagram().getNet().getLineClasses().size();
            }
        }

        @Override
        public Object getElementAt(int index) {
            if (model.getDiagram() == null) {
                return null;
            } else {
                return model.getDiagram().getNet().getLineClasses().get(index);
            }
        }

        public void updateInfo() {
            this.fireContentsChanged(this, 0, getSize());
        }

        public void addLineClass(LineClass clazz) {
            int size = getSize();
            model.getDiagram().getNet().addLineClass(clazz);
            this.fireIntervalAdded(this, size, size);
        }

        public void removeLineClass(int index) {
            LineClass clazz = (LineClass) getElementAt(index);
            model.getDiagram().getNet().removeLineClass(clazz);
            this.fireIntervalRemoved(model, index, index);
        }
        
        public void moveLineClass(int index1, int index2) {
            LineClass clazz = model.getDiagram().getNet().getLineClasses().get(index1);
            model.getDiagram().getNet().removeLineClass(clazz);
            model.getDiagram().getNet().addLineClass(clazz, index2);
            this.fireContentsChanged(this, index1, index1);
            this.fireContentsChanged(this, index2, index2);
        }
    }

    /** Creates new form LineClassesDialog */
    public LineClassesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setModel(ApplicationModel model) {
        this.model = model;
        listModel = new LineClassesListModel();
        lineClassesList.setModel(listModel);
    }

    public void updateValues() {
        // update list of available classes ...
        listModel.updateInfo();
        this.updateEnabled();
    }
    
    public void updateEnabled() {
        boolean enabled = !lineClassesList.isSelectionEmpty();
        upButton.setEnabled(enabled);
        downButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        lineClassesList = new javax.swing.JList();
        nameTextField = new javax.swing.JTextField();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

        lineClassesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lineClassesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lineClassesListValueChanged(evt);
            }
        });
        scrollPane.setViewportView(lineClassesList);

        newButton.setText(ResourceLoader.getString("button.new")); // NOI18N
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        deleteButton.setText(ResourceLoader.getString("button.delete")); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        upButton.setText("^");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText("v");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(upButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(downButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameTextField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
    if (nameTextField != null && !"".equals(nameTextField.getText())) {
        // create new LineClass
        LineClass lineClass = new LineClass(UUID.randomUUID().toString(), nameTextField.getText());
        listModel.addLineClass(lineClass);
        nameTextField.setText("");
        model.fireEvent(new ApplicationModelEvent(ApplicationModelEventType.LINE_CLASSES_CHANGED, model));
    }
}//GEN-LAST:event_newButtonActionPerformed

private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
    if (!lineClassesList.isSelectionEmpty()) {
        int selected = lineClassesList.getSelectedIndex();
        listModel.removeLineClass(selected);
        if (selected >= listModel.getSize()) {
            selected--;
        }
        lineClassesList.setSelectedIndex(selected);
        model.fireEvent(new ApplicationModelEvent(ApplicationModelEventType.LINE_CLASSES_CHANGED, model));
    }
}//GEN-LAST:event_deleteButtonActionPerformed

private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
    // move selected line class up
    if (!lineClassesList.isSelectionEmpty()) {
        int selected = lineClassesList.getSelectedIndex();
        selected -= 1;
        if (selected < 0)
            return;
        listModel.moveLineClass(selected + 1, selected);
        lineClassesList.setSelectedIndex(selected);
        model.fireEvent(new ApplicationModelEvent(ApplicationModelEventType.LINE_CLASSES_CHANGED, model));
    }
}//GEN-LAST:event_upButtonActionPerformed

private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
    // move selected line class down
    if (!lineClassesList.isSelectionEmpty()) {
        int selected = lineClassesList.getSelectedIndex();
        selected += 1;
        if (selected >= listModel.getSize())
            return;
        listModel.moveLineClass(selected - 1, selected);
        lineClassesList.setSelectedIndex(selected);
        model.fireEvent(new ApplicationModelEvent(ApplicationModelEventType.LINE_CLASSES_CHANGED, model));
    }
}//GEN-LAST:event_downButtonActionPerformed

private void lineClassesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lineClassesListValueChanged
    if (!evt.getValueIsAdjusting())
        this.updateEnabled();
}//GEN-LAST:event_lineClassesListValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton downButton;
    private javax.swing.JList lineClassesList;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton newButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}