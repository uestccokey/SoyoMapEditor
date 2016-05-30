/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditObjectPane.java
 *
 * Created on 2011-10-5, 22:31:45
 */
package com.soyostar.editor.map.ui.widget;

import com.soyostar.editor.map.main.AppData;
import com.soyostar.editor.map.model.MapObject;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Administrator
 */
public class EditObjectPane extends javax.swing.JPanel {

    /** Creates new form EditObjectPane */
    public EditObjectPane() {
        initComponents();
    }
    private MapObject object = null;

    public void setMapObject(MapObject object) {
        this.object = object;
        if (object == null) {
            objectNameTextField.setText("");
            objectTypeTextField.setText("");
            objectImageTextField.setText("");
            objectXTextField.setText("");
            objectYTextField.setText("");
            objectWTextField.setText("");
            objectHTextField.setText("");
        } else {
            objectNameTextField.setText(object.getName());
            objectTypeTextField.setText(object.getType());
            objectImageTextField.setText(object.getImageSource());
            objectXTextField.setText(object.getX() + "");
            objectYTextField.setText(object.getY() + "");
            objectWTextField.setText(object.getWidth() + "");
            objectHTextField.setText(object.getHeight() + "");
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

        jLabel1 = new javax.swing.JLabel();
        objectNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        objectTypeTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        objectImageTextField = new javax.swing.JTextField();
        objectAddImageSourceButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        objectXTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        objectYTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        objectWTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        objectHTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        addObjectPropertyButton = new javax.swing.JButton();
        removeObjectPropertyButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        objectPropertyTable = new javax.swing.JTable();
        objectApplyButton = new javax.swing.JButton();

        jLabel1.setText("名称");
        jLabel1.setName("jLabel1"); // NOI18N

        objectNameTextField.setName("objectNameTextField"); // NOI18N

        jLabel2.setText("类型");
        jLabel2.setName("jLabel2"); // NOI18N

        objectTypeTextField.setName("objectTypeTextField"); // NOI18N

        jLabel3.setText("图像");
        jLabel3.setName("jLabel3"); // NOI18N

        objectImageTextField.setName("objectImageTextField"); // NOI18N

        objectAddImageSourceButton.setText("...");
        objectAddImageSourceButton.setName("objectAddImageSourceButton"); // NOI18N
        objectAddImageSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objectAddImageSourceButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("范围"));
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel4.setText("x");
        jLabel4.setName("jLabel4"); // NOI18N

        objectXTextField.setName("objectXTextField"); // NOI18N

        jLabel5.setText("y");
        jLabel5.setName("jLabel5"); // NOI18N

        objectYTextField.setName("objectYTextField"); // NOI18N

        jLabel6.setText("宽度");
        jLabel6.setName("jLabel6"); // NOI18N

        objectWTextField.setName("objectWTextField"); // NOI18N

        jLabel7.setText("高度");
        jLabel7.setName("jLabel7"); // NOI18N

        objectHTextField.setName("objectHTextField"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(objectXTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(objectWTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(objectHTextField)
                    .addComponent(objectYTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {objectHTextField, objectWTextField, objectXTextField, objectYTextField});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel4, jLabel5, jLabel6, jLabel7});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(objectYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(objectHTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(objectXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(objectWTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {objectHTextField, objectWTextField, objectXTextField, objectYTextField});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jLabel5, jLabel6, jLabel7});

        jPanel2.setName("jPanel2"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setOrientation(javax.swing.JToolBar.VERTICAL);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        addObjectPropertyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/soyostar/resources/gnome-new.png"))); // NOI18N
        addObjectPropertyButton.setFocusable(false);
        addObjectPropertyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addObjectPropertyButton.setName("addObjectPropertyButton"); // NOI18N
        addObjectPropertyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addObjectPropertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addObjectPropertyButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(addObjectPropertyButton);

        removeObjectPropertyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/soyostar/resources/gnome-delete.png"))); // NOI18N
        removeObjectPropertyButton.setName("removeObjectPropertyButton"); // NOI18N
        removeObjectPropertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeObjectPropertyButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(removeObjectPropertyButton);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        objectPropertyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "属性", "值"
            }
        ));
        objectPropertyTable.setName("objectPropertyTable"); // NOI18N
        jScrollPane1.setViewportView(objectPropertyTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
        );

        objectApplyButton.setText("应用");
        objectApplyButton.setName("objectApplyButton"); // NOI18N
        objectApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objectApplyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(objectImageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(objectAddImageSourceButton, 0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(objectTypeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(objectNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(objectApplyButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(objectApplyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(objectNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(objectTypeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(objectImageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(objectAddImageSourceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void objectApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objectApplyButtonActionPerformed
        // TODO add your handling code here:
        if (object != null) {
            object.setName(objectNameTextField.getText());
            object.setType(objectTypeTextField.getText());
            object.setImageSource(objectImageTextField.getText());
            object.setX(Integer.parseInt(objectXTextField.getText()));
            object.setY(Integer.parseInt(objectYTextField.getText()));
            object.setWidth(Integer.parseInt(objectWTextField.getText()));
            object.setHeight(Integer.parseInt(objectHTextField.getText()));
            String path = objectImageTextField.getText();
            if (path != null && !path.equals("")) {
                object.setImageSource(path);
            }
            AppData.getInstance().getCurrentMap().getMapRender().repaint();
        }
    }//GEN-LAST:event_objectApplyButtonActionPerformed

    private void objectAddImageSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objectAddImageSourceButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser ch = new JFileChooser(AppData.getInstance().getCurProject().getPath() + File.separatorChar + "image" + File.separatorChar + "tileset");
        JFileChooserImagePreview preview = new JFileChooserImagePreview(ch);
        ch.addPropertyChangeListener(preview);
        ch.setAccessory(preview);
        int ret = ch.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            objectImageTextField.setText(ch.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_objectAddImageSourceButtonActionPerformed

    private void addObjectPropertyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addObjectPropertyButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addObjectPropertyButtonActionPerformed

    private void removeObjectPropertyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeObjectPropertyButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeObjectPropertyButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addObjectPropertyButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton objectAddImageSourceButton;
    private javax.swing.JButton objectApplyButton;
    private javax.swing.JTextField objectHTextField;
    private javax.swing.JTextField objectImageTextField;
    private javax.swing.JTextField objectNameTextField;
    private javax.swing.JTable objectPropertyTable;
    private javax.swing.JTextField objectTypeTextField;
    private javax.swing.JTextField objectWTextField;
    private javax.swing.JTextField objectXTextField;
    private javax.swing.JTextField objectYTextField;
    private javax.swing.JButton removeObjectPropertyButton;
    // End of variables declaration//GEN-END:variables
}
