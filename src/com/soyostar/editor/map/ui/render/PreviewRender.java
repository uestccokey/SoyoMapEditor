/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.ui.render;

import com.soyostar.editor.map.model.Layer;
import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.ObjectLayer;
import com.soyostar.editor.map.model.TileLayer;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public abstract class PreviewRender extends JPanel implements MouseMotionListener, MouseWheelListener {

    /**
     * 
     */
    protected double zoom = 1.0;                        //正常缩放级别为1
    /**
     * 
     */
    protected Map map;
    /**
     * 
     */
    protected boolean showScreen = false;
    /**
     *
     */
    protected boolean showSprite = true;
    private BufferedImage image;
    private Rectangle bound = new Rectangle(320, 480);  //虚拟屏幕框大小，默认为320*480
    /**
     * 
     */
    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0x3B3B3B);       //背景色
    /**
     *
     */
    protected static double[] zoomLevels = {
        0.0625, 0.125, 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 3.0, 4.0
    };
    /**
     * 
     */
    public static final int ZOOM_NORMALSIZE = 5;
    /**
     * 
     */
    protected int zoomLevel = ZOOM_NORMALSIZE;      //初始话为正常缩放级别

    /**
     * 
     * @param map
     */
    public PreviewRender(Map map) {
        setMap(map);
        image = new BufferedImage(map.getTileWidth() * map.getWidth(),
                map.getTileHeight() * map.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    /**
     *
     * @return
     */
    public boolean zoomIn() {
        if (zoomLevel < zoomLevels.length - 1) {
            setZoomLevel(zoomLevel + 1);
        }

        return zoomLevel < zoomLevels.length - 1;
    }

    /**
     *
     * @return
     */
    public boolean zoomOut() {
        if (zoomLevel > 0) {
            setZoomLevel(zoomLevel - 1);
        }

        return zoomLevel > 0;
    }

    /**
     *
     * @param zoom
     */
    public void setZoom(double zoom) {
        if (zoom > 0) {
            this.zoom = zoom;
            //revalidate();
            setSize(getPreferredSize());
        }
    }

    /**
     *
     * @param zoomLevel
     */
    public void setZoomLevel(int zoomLevel) {
        if (zoomLevel >= 0 && zoomLevel < zoomLevels.length) {
            this.zoomLevel = zoomLevel;
            setZoom(zoomLevels[zoomLevel]);
        }
    }

    /**
     * 
     * @param map
     */
    public void setMap(Map map) {
        map.setPreviewRender(this);
        this.map = map;
    }

    /**
     * 
     * @param w
     * @param h
     */
    public void setBound(int w, int h) {
        bound.width = w;
        bound.height = h;
        repaint();
    }

    /**
     * 
     * @return
     */
    public BufferedImage getBufferedImage() {
        return image;
    }

    /**
     * 
     * @return
     */
    public boolean isShowScreen() {
        return showScreen;
    }

    /**
     * 
     * @param showScreen
     */
    public void setShowScreen(boolean showScreen) {
        this.showScreen = showScreen;
    }

    /**
     * 
     * @return
     */
    public boolean isShowSprite() {
        return showSprite;
    }

    /**
     * 
     * @param showSprite
     */
    public void setShowSprite(boolean showSprite) {
        this.showSprite = showSprite;
    }

    /**
     *
     * @return
     */
    protected Dimension getTileSize() {
        return new Dimension(
                (int) (map.getTileWidth() * zoom),
                (int) (map.getTileHeight() * zoom));
    }

    @Override
    public abstract Dimension getPreferredSize();

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(DEFAULT_BACKGROUND_COLOR);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        paintMap(g2d);
        g.drawImage(image, 0, 0, null);
        if (showScreen) {
            paintScreen(g);
        }
    }

    /**
     * 
     * @param g
     */
    protected void paintScreen(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_ATOP, 0.25f));
        g.setColor(new Color(100, 100, 255));
        g.fill3DRect((int) (bound.x * zoom), (int) (bound.y * zoom), (int) (bound.width * zoom), (int) (bound.height * zoom), true);
        g.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0F, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER));
        g.drawRect((int) (bound.x * zoom), (int) (bound.y * zoom), (int) (bound.width * zoom), (int) (bound.height * zoom));
    }

    /**
     * 
     * @param g2d
     */
    protected void paintMap(Graphics2D g2d) {
        Iterator li = map.getLayers();
        Layer layer;
        while (li.hasNext()) {
            layer = (Layer) li.next();
            if (layer != null) {
                g2d.setComposite(AlphaComposite.SrcOver);
                if (layer instanceof TileLayer) {
                    paintTileLayer(g2d, (TileLayer) layer);
                
                } else if (layer instanceof ObjectLayer) {
                    if (showSprite) {
                        paintObjectLayer(g2d, (ObjectLayer) layer);
                    }
                }
            }
        }
    }

    /**
     * 
     * @param layer
     * @return
     */
    protected static Layer createLayerCopy(Layer layer) {
        try {
            return (Layer) layer.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Draws a TileLayer. Implemented in a subclass.
     *
     * @param g2d   the graphics context to draw the layer onto
     * @param layer the TileLayer to be drawn
     */
    protected abstract void paintTileLayer(Graphics2D g2d, TileLayer layer);//画图层


    /**
     * 
     * @param g2d
     * @param layer
     */
    protected abstract void paintObjectLayer(Graphics2D g2d, ObjectLayer layer);//画对象层

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public abstract Point screenToTileCoords(int x, int y);             //屏幕坐标转为瓷砖位置

    /**
     * Returns the pixel coordinates on the map based on the given screen
     * coordinates. The map pixel coordinates may be different in more ways
     * than the zoom level, depending on the projection the view implements.
     *
     * @param x x in screen coordinates
     * @param y y in screen coordinates
     * @return the position in map pixel coordinates
     */
    public abstract Point screenToPixelCoords(int x, int y);            //屏幕坐标转为像素坐标

    /**
     * Returns the location on the screen of the top corner of a tile.
     *
     * @param x
     * @param y
     * @return Point
     */
    public abstract Point tileToScreenCoords(int x, int y);             //瓷砖位置转为屏幕坐标

    public void mouseDragged(MouseEvent e) {
        bound.x = e.getX() - bound.width / 2;
        bound.y = e.getY() - bound.height / 2;
        repaint();
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     * @param e 
     */
    public void mouseMoved(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            setZoomLevel(zoomLevel + e.getWheelRotation());
            this.repaint();
        }
    }
}
