/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewTileSetDialog.java
 *
 * Created on 2011-3-19, 22:15:09
 */
package com.soyostar.editor.map.dialog;

import com.soyostar.editor.map.main.AppData;
import com.soyostar.editor.map.model.TileSet;
import com.soyostar.editor.map.ui.widget.JFileChooserImagePreview;
import com.soyostar.editor.util.TileCutter;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class NewTileSetDialog extends javax.swing.JDialog {

    /** Creates new form NewTileSetDialog
     * @param parent 
     * @param modal 
     */
    public NewTileSetDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initialize();
    }

    private void initialize() {
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(okButton);
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
        tilesetNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tilesetPathTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancleButton = new javax.swing.JButton();
        pathButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(NewTileSetDialog.class);
        setTitle(resourceMap.getString("title")); // NOI18N
        setResizable(false);

        jLabel1.setText("图集名称");
        jLabel1.setName("jLabel1"); // NOI18N

        tilesetNameTextField.setName("tilesetNameTextField"); // NOI18N

        jLabel2.setText("图集路径");
        jLabel2.setName("jLabel2"); // NOI18N

        tilesetPathTextField.setName("tilesetPathTextField"); // NOI18N

        okButton.setText("确定");
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancleButton.setText("取消");
        cancleButton.setName("cancleButton"); // NOI18N
        cancleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancleButtonActionPerformed(evt);
            }
        });

        pathButton.setText("浏览");
        pathButton.setName("pathButton"); // NOI18N
        pathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pathButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tilesetPathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pathButton))
                            .addComponent(tilesetNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(okButton)
                        .addGap(71, 71, 71)
                        .addComponent(cancleButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tilesetNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tilesetPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pathButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancleButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //压缩png图片

//    private void compress(String path) {
//        Vector<File> filev = new Vector<File>();
//        filev.add(new File(path));
//        PngCompress compiler = new PngCompress(filev);
//        try {
//            compiler.compile();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        System.out.println("before compress,total size = "
//            + compiler.sizeBefore + ",after compress total size = "
//            + compiler.sizeAfter
//            + "总共节省了 " + (compiler.sizeBefore - compiler.sizeAfter) + " 字节空间");
//    }
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:

        System.out.println("name:" + tilesetNameTextField.getText());
        System.out.println("file:" + tilesetPathTextField.getText());
        String[] names = tilesetNameTextField.getText().split("\\|");
        String[] paths = tilesetPathTextField.getText().split("\\|");
        for (int n = 0; n < paths.length; n++) {
            TileSet newTileset = new TileSet();
            String name, path;
            if (names[n].equals("")) {
                name = "未命名";
            } else {
                name = names[n];
            }
            if (paths[n].equals("")) {
                JOptionPane.showMessageDialog(this, "路径不能为空！");
                return;
            } else {
                path = paths[n];
            }
            newTileset.setName(name);
            try {
                newTileset.importTileBitmap(path, new TileCutter(data.getCurrentMap().getTileWidth(), data.getCurrentMap().getTileHeight()));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(),
                        "加载图集失败！" + paths[n], JOptionPane.WARNING_MESSAGE);
            }
            if (data.addTileSet(newTileset)) {
                System.out.println("加载图元" + newTileset.getName() + "成功!");
            } else {
                JOptionPane.showMessageDialog(this, "添加图集失败！" + paths[n]);
            }
        }
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    private AppData data = AppData.getInstance();
    private void cancleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancleButtonActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_cancleButtonActionPerformed

    private void pathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pathButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser ch = new JFileChooser(AppData.getInstance().getCurProject().getPath() + File.separatorChar + "image" + File.separatorChar + "tileset");
        ch.setMultiSelectionEnabled(true);
        JFileChooserImagePreview preview = new JFileChooserImagePreview(ch);
        ch.addPropertyChangeListener(preview);
        ch.setAccessory(preview);
        int ret = ch.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File[] files = ch.getSelectedFiles();
            StringBuilder sbFileName = new StringBuilder();
            for (int i = 0; i < files.length; i++) {
                sbFileName.append(files[i].getName());
                if (i != files.length - 1) {
                    sbFileName.append("|");
                }
            }
            StringBuilder sbFilePath = new StringBuilder();
            for (int i = 0; i < files.length; i++) {
                sbFilePath.append(files[i].getAbsolutePath());
                if (i != files.length - 1) {
                    sbFilePath.append("|");
                }
            }
            tilesetPathTextField.setText(sbFilePath.toString());
            tilesetNameTextField.setText(sbFileName.toString());
        }
    }//GEN-LAST:event_pathButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                NewTileSetDialog dialog = new NewTileSetDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton cancleButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton okButton;
    private javax.swing.JButton pathButton;
    private javax.swing.JTextField tilesetNameTextField;
    private javax.swing.JTextField tilesetPathTextField;
    // End of variables declaration//GEN-END:variables
}