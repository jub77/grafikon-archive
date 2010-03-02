package net.parostroj.timetable.gui.components;

import net.parostroj.timetable.gui.utils.ResourceLoader;

/**
 * Changes tracker panel.
 *
 * @author jub
 */
public class ChangesTrackerPanel extends javax.swing.JPanel {

    /** Creates new form ChangesTrackerPanel */
    public ChangesTrackerPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane();
        eventsTextArea = new javax.swing.JTextArea();
        javax.swing.JPanel buttonsPanel = new javax.swing.JPanel();
        javax.swing.JButton clearButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        eventsTextArea.setColumns(20);
        eventsTextArea.setRows(5);
        scrollPane.setViewportView(eventsTextArea);

        add(scrollPane, java.awt.BorderLayout.CENTER);

        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        clearButton.setText(ResourceLoader.getString("button.delete")); // NOI18N
        buttonsPanel.add(clearButton);

        add(buttonsPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea eventsTextArea;
    // End of variables declaration//GEN-END:variables

}