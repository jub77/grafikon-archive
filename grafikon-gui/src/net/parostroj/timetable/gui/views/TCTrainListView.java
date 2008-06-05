/*
 * ECTrainListView.java
 *
 * Created on 12. září 2007, 16:07
 */
package net.parostroj.timetable.gui.views;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import net.parostroj.timetable.actions.TrainComparator;
import net.parostroj.timetable.actions.TrainSort;
import net.parostroj.timetable.gui.ApplicationModel;
import net.parostroj.timetable.gui.ApplicationModelEvent;
import net.parostroj.timetable.gui.ApplicationModelEventType;
import net.parostroj.timetable.gui.ApplicationModelListener;
import net.parostroj.timetable.model.TrainsCycle;
import net.parostroj.timetable.model.Train;
import net.parostroj.timetable.model.TrainsCycleItem;
import net.parostroj.timetable.utils.ResourceLoader;

/**
 * View with list of train on one side and list of train of the engine cycle
 * on the other.
 * 
 * @author jub
 */
public class TCTrainListView extends javax.swing.JPanel implements ApplicationModelListener, TrainSelector {
    
    private ApplicationModel model;
    
    private TCDelegate delegate;
    
    private TrainSort sort;
    
    /** Creates new form ECTrainListView */
    public TCTrainListView() {
        initComponents();
    }
    
    public void setModel(ApplicationModel model, TCDelegate delegate) {
        model.addListener(this);
        this.model = model;
        this.delegate = delegate;
        
        this.updateListAllTrains();
    }

    @Override
    public void modelChanged(ApplicationModelEvent event) {
        TCDelegate.Action action = delegate.transformEventType(event.getType());
        switch (event.getType()) {
            case SET_DIAGRAM_CHANGED:
                if (model.getDiagram() != null) {
                    this.sort = new TrainSort(
                            new TrainComparator(
                                TrainComparator.Type.ASC,
                                model.getDiagram().getTrainsData().getTrainSortPattern()
                            ));
                }
                this.updateListAllTrains();
                break;
            case NEW_TRAIN:
                this.updateListAllTrains();
                break;
            case DELETE_TRAIN:
                this.updateListEngineCycle();
                this.updateListAllTrains();
                break;
            default:
                // do nothing
                break;
        }
        if (action != null) {
            switch (action) {
                case SELECTED_CHANGED:
                    this.updateListEngineCycle();
                    this.updateErrors();
                    break;
                case DELETE_CYCLE:
                    this.updateListAllTrains();
                    break;
                default:
                    // nothing
            }
        }
    }
    
    private void updateListAllTrains() {
        // left list with available trains
        if (model.getDiagram() == null)
            allTrainsList.setModel(new DefaultListModel());
        else {
            // get all trains (sort)
            List<Train> getTrains = new ArrayList<Train>();
            for (Train train : model.getDiagram().getTrains()) {
                if (delegate.getTrainCycles(train).isEmpty())
                    getTrains.add(train);
            }
            // sort them
            getTrains = sort.sort(getTrains);
            
            DefaultListModel m = new DefaultListModel();
            for (Train train : getTrains) {
                m.addElement(new TrainWrapper(train,TrainWrapper.Type.NAME_AND_END_NODES_WITH_TIME));
            }
            allTrainsList.setModel(m);
        }
    }
    
    private void updateListEngineCycle() {
        // right list with assign trains
        if (delegate.getSelectedCycle(model) == null)
            ecTrainsList.setModel(new DefaultListModel());
        else {
            DefaultListModel m = new DefaultListModel();
            for (TrainsCycleItem item : delegate.getSelectedCycle(model)) {
                Train train = item.getTrain();
                m.addElement(new TrainWrapper(train,TrainWrapper.Type.NAME_AND_END_NODES_WITH_TIME));
            }
            ecTrainsList.setModel(m);
        }
    }
    
    private void updateErrors() {
        TrainsCycle selectedCycle = delegate.getSelectedCycle(model);
        if (selectedCycle != null) {
            infoTextArea.setText(delegate.getTrainCycleErrors(selectedCycle));
        } else {
            infoTextArea.setText("");
        }
    }
    
    private Train lastSelected;
    
    @Override
    public void selectTrain(Train train) {
        allTrainsList.setSelectedValue(new TrainWrapper(train, TrainWrapper.Type.NAME_AND_END_NODES_WITH_TIME), true);
        ecTrainsList.setSelectedValue(new TrainWrapper(train, TrainWrapper.Type.NAME_AND_END_NODES_WITH_TIME), true);
        lastSelected = train;
    }

    @Override
    public Train getSelectedTrain() {
        return lastSelected;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane1 = new javax.swing.JScrollPane();
        allTrainsList = new javax.swing.JList();
        scrollPane2 = new javax.swing.JScrollPane();
        ecTrainsList = new javax.swing.JList();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        scrollPane3 = new javax.swing.JScrollPane();
        infoTextArea = new javax.swing.JTextArea();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        detailsTextField = new javax.swing.JTextField();
        changeButton = new javax.swing.JButton();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();

        allTrainsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        allTrainsList.setPrototypeCellValue("mmmmmmmmmmmmm");
        scrollPane1.setViewportView(allTrainsList);

        ecTrainsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ecTrainsList.setPrototypeCellValue("mmmmmmmmmmmmm");
        ecTrainsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ecTrainsListValueChanged(evt);
            }
        });
        scrollPane2.setViewportView(ecTrainsList);

        addButton.setText(ResourceLoader.getString("ec.trains.add")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText(ResourceLoader.getString("ec.trains.remove")); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        infoTextArea.setColumns(20);
        infoTextArea.setFont(addButton.getFont());
        infoTextArea.setRows(3);
        scrollPane3.setViewportView(infoTextArea);

        upButton.setText(ResourceLoader.getString("ec.trains.up")); // NOI18N
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText(ResourceLoader.getString("ec.trains.down")); // NOI18N
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        changeButton.setText(ResourceLoader.getString("ec.details.change")); // NOI18N
        changeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(ResourceLoader.getString("ec.list.comment")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(upButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(downButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane2))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changeButton))
            .addComponent(scrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addGap(52, 52, 52)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton))
                    .addComponent(scrollPane2)
                    .addComponent(scrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeButton)
                    .addComponent(detailsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane3))
        );
    }// </editor-fold>//GEN-END:initComponents

private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
    // change order
    TrainWrapper selected = (TrainWrapper)ecTrainsList.getSelectedValue();
    int selectedIndex = ecTrainsList.getSelectedIndex();
    if (selected != null) {
        Train t = selected.getTrain();
        int newIndex = selectedIndex + 1;
        if (newIndex < delegate.getTrainCycles(t).get(0).getCycle().getItems().size()) {
            // remove ...
            DefaultListModel m = (DefaultListModel)ecTrainsList.getModel();
            m.remove(selectedIndex);
            TrainsCycleItem mItem = delegate.getTrainCycles(t).get(0).getCycle().removeItem(selectedIndex);
            // move to new place
            m.add(newIndex, selected);
            delegate.getSelectedCycle(model).addItem(mItem, newIndex);
            this.updateErrors();
            ecTrainsList.setSelectedValue(selected, true);
            ecTrainsList.repaint();
        }
    }    
}//GEN-LAST:event_downButtonActionPerformed

private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
    // change order
    TrainWrapper selected = (TrainWrapper)ecTrainsList.getSelectedValue();
    int selectedIndex = ecTrainsList.getSelectedIndex();
    if (selected != null) {
        Train t = selected.getTrain();
        int newIndex = selectedIndex - 1;
        if (newIndex >= 0) {
            // remove ...
            DefaultListModel m = (DefaultListModel)ecTrainsList.getModel();
            m.remove(selectedIndex);
            TrainsCycleItem mItem = delegate.getTrainCycles(t).get(0).getCycle().removeItem(selectedIndex);
            // move to new place
            m.add(newIndex, selected);
            delegate.getTrainCycles(t).get(0).getCycle().addItem(mItem, newIndex);
            this.updateErrors();
            ecTrainsList.setSelectedValue(selected, true);
            ecTrainsList.repaint();
        }
    }    
}//GEN-LAST:event_upButtonActionPerformed

private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
    TrainWrapper selected = (TrainWrapper)ecTrainsList.getSelectedValue();
    if (selected != null) {
        Train t = selected.getTrain();
        TrainsCycleItem item = delegate.getTrainCycles(t).get(0);
        t.removeCycle(item);
        item.getCycle().removeItem(item);
        this.updateListAllTrains();
        this.updateListEngineCycle();
        this.updateErrors();
        model.fireEvent(new ApplicationModelEvent(ApplicationModelEventType.MODIFIED_TRAIN, model, t));

        delegate.fireEvent(TCDelegate.Action.MODIFIED_CYCLE, model, delegate.getSelectedCycle(model));
    }
}//GEN-LAST:event_removeButtonActionPerformed

private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
    TrainWrapper selected = (TrainWrapper)allTrainsList.getSelectedValue();
    if (selected != null) {
        Train t = selected.getTrain();
        TrainsCycle cycle = delegate.getSelectedCycle(model);
        if (cycle != null) {
            TrainsCycleItem item = new TrainsCycleItem(cycle, t, null);
            cycle.addItem(item);
            t.addCycle(item);
            this.updateListAllTrains();
            this.updateListEngineCycle();
            this.updateErrors();
            model.fireEvent(new ApplicationModelEvent(ApplicationModelEventType.MODIFIED_TRAIN, model, t));
            
            delegate.fireEvent(TCDelegate.Action.MODIFIED_CYCLE, model, delegate.getSelectedCycle(model));
        }
    }
}//GEN-LAST:event_addButtonActionPerformed

private void changeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeButtonActionPerformed
    if (ecTrainsList.getSelectedIndex() != -1) {
        TrainsCycleItem item = delegate.getSelectedCycle(model).getItems().get(ecTrainsList.getSelectedIndex());
        item.setComment(detailsTextField.getText());
    }
}//GEN-LAST:event_changeButtonActionPerformed

private void ecTrainsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ecTrainsListValueChanged
    if (!evt.getValueIsAdjusting()) {
        if (ecTrainsList.getSelectedIndex() == -1) {
            detailsTextField.setText("");
        } else {
            TrainsCycleItem item = delegate.getSelectedCycle(model).getItems().get(ecTrainsList.getSelectedIndex());
            detailsTextField.setText(item.getComment());
        }
    }
}//GEN-LAST:event_ecTrainsListValueChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JList allTrainsList;
    private javax.swing.JButton changeButton;
    private javax.swing.JTextField detailsTextField;
    private javax.swing.JButton downButton;
    private javax.swing.JList ecTrainsList;
    private javax.swing.JTextArea infoTextArea;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JScrollPane scrollPane2;
    private javax.swing.JScrollPane scrollPane3;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

}
