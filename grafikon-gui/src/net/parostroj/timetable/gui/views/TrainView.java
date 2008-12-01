/*
 * TrainView.java
 *
 * Created on 31. srpen 2007, 12:50
 */
package net.parostroj.timetable.gui.views;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import net.parostroj.timetable.gui.ApplicationModel;
import net.parostroj.timetable.gui.ApplicationModelEvent;
import net.parostroj.timetable.gui.ApplicationModelEventType;
import net.parostroj.timetable.gui.ApplicationModelListener;
import net.parostroj.timetable.gui.dialogs.CopyTrainDialog;
import net.parostroj.timetable.gui.dialogs.EditTrainDialog;
import net.parostroj.timetable.model.TimeInterval;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.utils.ResourceLoader;

/**
 * View of train details.
 * 
 * @author  jub
 */
public class TrainView extends javax.swing.JPanel implements ApplicationModelListener {
    
    private ApplicationModel model;
    
    private Train train;
    
    private EditTrainDialog editDialog;
    
    /**
     * Creates new form TrainView.
     */
    public TrainView() {
        initComponents();
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        for (TrainTableColumn column : TrainTableColumn.values()) {
            if (column.isRightAling())
                trainTable.getColumnModel().getColumn(column.getIndex()).setCellRenderer(cellRenderer);
            if (column.getEditor() != null) {
                trainTable.getColumnModel().getColumn(column.getIndex()).setCellEditor(column.getEditor());
            }
            this.setTableColumnWidths(trainTable, column.getIndex(), column.getMinWidth(), column.getMaxWidth(), column.getPrefWidth());
        }
        
        editDialog = new EditTrainDialog((java.awt.Frame)this.getTopLevelAncestor(), true);
    }
    
    private void setTableColumnWidths(JTable table, int column, int min, int max, int preffered) {
        TableColumn c = table.getColumnModel().getColumn(column);
        c.setWidth(preffered);
        c.setMinWidth(min);
        c.setMaxWidth(max);
    }

    public void setModel(ApplicationModel model) {
        this.model = model;
        this.train = model.getSelectedTrain();
        this.updateView();
        this.model.addListener(this);
        ((TrainTableModel)trainTable.getModel()).setModel(model);
        editDialog.setModel(model);
    }
    

    @Override
    public void modelChanged(ApplicationModelEvent event) {
        if (event.getType() == ApplicationModelEventType.SELECTED_TRAIN_CHANGED || event.getType() == ApplicationModelEventType.SET_DIAGRAM_CHANGED) {
            this.train = model.getSelectedTrain();
            this.updateView();
        } else if ((event.getType() == ApplicationModelEventType.MODIFIED_TRAIN_NAME_TYPE || event.getType() == ApplicationModelEventType.MODIFIED_TRAIN) && event.getObject() == model.getSelectedTrain()) {
            this.updateView();
        }
    }
    
    private void updateView() {
        if (train == null) {
            trainTextField.setText(null);
            speedTextField.setText(null);
            techTimeTextField.setText(null);
            speedTextField.setEnabled(false);
            
            editButton.setEnabled(false);
            copyButton.setEnabled(false);
        } else {
            // train type
            trainTextField.setText(train.getCompleteName());
            speedTextField.setText(Integer.toString(train.getTopSpeed()));
            techTimeTextField.setText(this.createTechTimeString(train));
            speedTextField.setEnabled(true);

            editButton.setEnabled(true);
            copyButton.setEnabled(true);
        }

        trainTable.removeEditor();
        ((TrainTableModel)trainTable.getModel()).setTrain(train);
        
        this.invalidate();
    }

    private String createTechTimeString(Train train) {
        String before = ResourceLoader.getString("create.train.time.before") + ": " + Integer.toString(train.getTimeBefore() / 60);
        String after = ResourceLoader.getString("create.train.time.after") + ": " + Integer.toString(train.getTimeAfter() / 60);
        if (train.getTimeIntervalBefore() != null)
            System.out.println("TEST: " + train.getTimeIntervalBefore().isOverlapping());
        if (train.getTimeIntervalAfter() != null)
            System.out.println("TEST(2): " + train.getTimeIntervalAfter().isOverlapping());

        if (train.getTimeIntervalBefore() != null && train.getTimeIntervalBefore().isOverlapping()) {
            before = before + " [" + this.getConflicts(train.getTimeIntervalBefore()) + "]";
        }
        if (train.getTimeIntervalAfter() != null && train.getTimeIntervalAfter().isOverlapping()) {
            after = after + " [" + this.getConflicts(train.getTimeIntervalAfter()) + "]";
        }
        return "(" + before + ", " + after + ")";
    }
    
    private String getConflicts(TimeInterval interval) {
        StringBuilder builder = new StringBuilder();
        for (TimeInterval overlap : interval.getOverlappingIntervals()) {
            if (builder.length() != 0)
                builder.append(", ");
            builder.append(overlap.getTrain().getName());
        }
        return builder.toString();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        trainTextField = new javax.swing.JTextField();
        trainTableScrollPane = new javax.swing.JScrollPane();
        trainTable = new javax.swing.JTable();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        speedTextField = new javax.swing.JTextField();
        editButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        techTimeTextField = new javax.swing.JTextField();

        jLabel1.setText(ResourceLoader.getString("create.train.number")); // NOI18N

        trainTextField.setEditable(false);

        trainTable.setModel(new TrainTableModel(model,train));
        trainTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        trainTableScrollPane.setViewportView(trainTable);

        jLabel2.setText(ResourceLoader.getString("create.train.speed")); // NOI18N

        speedTextField.setColumns(5);
        speedTextField.setEditable(false);

        editButton.setText(ResourceLoader.getString("button.edit")); // NOI18N
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        copyButton.setText(ResourceLoader.getString("button.copy")); // NOI18N
        copyButton.setEnabled(false);
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });

        jLabel3.setText(ResourceLoader.getString("create.train.technological.time")); // NOI18N

        techTimeTextField.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(trainTableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(speedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(techTimeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(copyButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editButton))
                            .addComponent(trainTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(trainTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(editButton)
                    .addComponent(copyButton)
                    .addComponent(speedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(techTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trainTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
    editDialog.getSelectedTrainData();
    editDialog.setLocationRelativeTo(this);
    editDialog.setVisible(true);
}//GEN-LAST:event_editButtonActionPerformed

private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
    CopyTrainDialog dialog = new CopyTrainDialog((java.awt.Frame)this.getTopLevelAncestor(), true, model, train);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}//GEN-LAST:event_copyButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton copyButton;
    private javax.swing.JButton editButton;
    private javax.swing.JTextField speedTextField;
    private javax.swing.JTextField techTimeTextField;
    private javax.swing.JTable trainTable;
    private javax.swing.JScrollPane trainTableScrollPane;
    private javax.swing.JTextField trainTextField;
    // End of variables declaration//GEN-END:variables

}
