/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * 
 * @author Administrator
 */
public class JFileChooserImagePreview extends JPanel implements PropertyChangeListener {

    private JFileChooser jfc;
    private Image img;

    /**
     * 
     * @param jfc
     */
    public JFileChooserImagePreview(JFileChooser jfc) {
        this.jfc = jfc;
        Dimension sz = new Dimension(200, 200);
        setPreferredSize(sz);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        try {
            File file = jfc.getSelectedFile();
            updateImage(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 
     * @param file
     * @throws IOException
     */
    public void updateImage(File file) throws IOException {
        if (file == null) {
            return;
        }
        img = ImageIO.read(file);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (img != null) {
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            String dim = w + " x " + h;
            int side = Math.max(w, h);
            double scale = 200.0 / (double) side;
            w = (int) (scale * (double) w);
            h = (int) (scale * (double) h);
            //将这个Image画出来
            g.drawImage(img, 0, 0, w, h, null);
            g.setColor(Color.black);
            g.drawString(dim, 11, 16);
            g.setColor(Color.white);
            g.drawString(dim, 10, 15);
            g.setColor(Color.black);
        }
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        JFileChooser jfc = new JFileChooser();
        JFileChooserImagePreview preview = new JFileChooserImagePreview(jfc);
        jfc.addPropertyChangeListener(preview);
        jfc.setAccessory(preview);
        jfc.showOpenDialog(null);
    }
}
