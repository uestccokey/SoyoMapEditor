/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LayerPropertyDialog.java
 *
 * Created on 2011-9-25, 22:01:13
 */
package com.soyostar.editor.map.dialog;

import com.soyostar.editor.map.main.AppData;
import com.soyostar.editor.map.model.Layer;
import java.util.Iterator;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class EditLayerDialog extends javax.swing.JDialog {

    /** Creates new form LayerPropertyDialog */
    public EditLayerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initialize();
    }

    private void initialize() {
        setLocationRelativeTo(null);
    }
    private Layer layer;

    public void setLayer(Layer layer) {
        this.layer = layer;
        layerNameTextField.setText(layer.getName());
        for (int i = 0; i < layerPropertyTable.getRowCount(); i++) {
            ((DefaultTableModel) layerPropertyTable.getModel()).removeRow(0);
        }
        Iterator iter = layer.getProperties().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            ((DefaultTableModel) layerPropertyTable.getModel()).addRow(new String[]{key.toString(), val.toString()});
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        addLayerPropertyButton = new javax.swing.JButton();
        removeLayerPropertyButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        layerPropertyTable = new javax.swing.JTable();
        okButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        layerNameTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(EditLayerDialog.class);
        setTitle(resourceMap.getString("title")); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        addLayerPropertyButton.setText("增加属性");
        addLayerPropertyButton.setFocusable(false);
        addLayerPropertyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addLayerPropertyButton.setName("addLayerPropertyButton"); // NOI18N
        addLayerPropertyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addLayerPropertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLayerPropertyButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(addLayerPropertyButton);

        removeLayerPropertyButton.setText("删除属性");
        removeLayerPropertyButton.setFocusable(false);
        removeLayerPropertyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeLayerPropertyButton.setName("removeLayerPropertyButton"); // NOI18N
        removeLayerPropertyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        removeLayerPropertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLayerPropertyButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(removeLayerPropertyButton);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        layerPropertyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "图层属性", "值"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        layerPropertyTable.setName("layerPropertyTable"); // NOI18N
        jScrollPane1.setViewportView(layerPropertyTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
        );

        okButton.setText("确定");
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        closeButton.setText("关闭");
        closeButton.setName("closeButton"); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("图层名称");
        jLabel1.setName("jLabel1"); // NOI18N

        layerNameTextField.setName("layerNameTextField"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(okButton)
                .addGap(35, 35, 35)
                .addComponent(closeButton)
                .addContainerGap(103, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(layerNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(layerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(closeButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:
        layer.setName(layerNameTextField.getText());
        layer.removeAllProperty();
        for (int i = 0; i < layerPropertyTable.getRowCount(); i++) {
            layer.addProperty(layerPropertyTable.getModel().getValueAt(i, 0).toString(), layerPropertyTable.getModel().getValueAt(i, 1).toString());
        }
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void addLayerPropertyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLayerPropertyButtonActionPerformed
        // TODO add your handling code here:
        ((DefaultTableModel) layerPropertyTable.getModel()).addRow(new String[]{"", ""});
    }//GEN-LAST:event_addLayerPropertyButtonActionPerformed

    private void removeLayerPropertyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLayerPropertyButtonActionPerformed
        // TODO add your handling code here:
        if (layerPropertyTable.getSelectedRow() != -1) {
            ((DefaultTableModel) layerPropertyTable.getModel()).removeRow(layerPropertyTable.getSelectedRow());
        }
    }//GEN-LAST:event_removeLayerPropertyButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                EditLayerDialog dialog = new EditLayerDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLayerPropertyButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField layerNameTextField;
    private javax.swing.JTable layerPropertyTable;
    private javax.swing.JButton okButton;
    private javax.swing.JButton removeLayerPropertyButton;
    // End of variables declaration//GEN-END:variables
}