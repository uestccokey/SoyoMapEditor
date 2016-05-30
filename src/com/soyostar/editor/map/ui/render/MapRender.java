/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.ui.render;

import com.soyostar.editor.map.ui.brush.AbBrush;
import com.soyostar.editor.map.ui.brush.CustomBrush;
import com.soyostar.editor.config.Configuration;
import com.soyostar.editor.map.main.AppData;
import com.soyostar.editor.map.model.Layer;
import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.MapObject;
import com.soyostar.editor.map.model.TileLayer;
import com.soyostar.editor.map.model.SelectionLayer;
import com.soyostar.editor.map.model.ObjectLayer;
import com.soyostar.editor.map.model.MapTile;
import com.soyostar.editor.map.uodoredo.LayerEdit;
import com.soyostar.editor.map.uodoredo.UndoRedoHandler;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.undo.UndoableEditSupport;

/**
 * 地图绘制器
 * @author Administrator
 */
public abstract class MapRender extends JPanel implements MouseListener, MouseMotionListener, Scrollable, MouseWheelListener {

    /**
     * 
     */
    public static final int ZOOM_NORMALSIZE = 5;
    /**
     * 
     */
    protected double zoom = 1.0;                    //正常缩放级别为1
    /**
     * 
     */
    protected int zoomLevel = ZOOM_NORMALSIZE;      //初始话为正常缩放级别
    /**
     * 
     */
    protected Map map;                              //所属地图
    /**
     * 
     */
    protected boolean showGrid = false;             //是否显示网格
    /**
     * 
     */
    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0x3B3B3B);       //背景色
    /**
     * 
     */
    public static final Color DEFAULT_GRID_COLOR = new Color(0, 0, 0);              //网格颜色
    /**
     * 
     */
    protected AbBrush currentBrush;
    /**
     *
     */
    protected static double[] zoomLevels = {
        0.0625, 0.125, 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0
    };
    private LayerEdit paintEdit;
    private final UndoRedoHandler undoHandler;
    private final UndoableEditSupport undoSupport;
    private BufferedImage miniMapImg = new BufferedImage(AppData.MINI_MAP_W, AppData.MINI_MAP_H, BufferedImage.TYPE_INT_ARGB);

    public BufferedImage getMiniMapImg() {
        return miniMapImg;
    }

    /**
     *
     * @param map
     */
    protected MapRender(Map map) {
        this();
        cursorSelectionLayer.select(0, 0);
        cursorSelectionLayer.setIsVisible(true);
        setMap(map);
    }

    /**
     *
     */
    protected MapRender() {
        undoHandler = new UndoRedoHandler(this);
        undoSupport = new UndoableEditSupport();
        undoSupport.addUndoableEditListener(undoHandler);
        this.setOpaque(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    /**
     * Sets a new brush. The brush can draw a preview of the change while
     * editing.
     * @param brush the new brush
     */
    public void setBrush(AbBrush brush) {
        currentBrush = brush;
    }

    /**
     * 
     * @return
     */
    public AbBrush getBrush() {
        return currentBrush;
    }

    /**
     *
     * @return
     */
    public Map getMap() {
        return map;
    }

    /**
     *
     * @param map
     */
    public void setMap(Map map) {
        this.map = map;
        map.setMapRender(this);
    }

    /**
     *
     * @return
     */
    public boolean isShowGrid() {
        return showGrid;
    }

    /**
     *
     * @param showGrid
     */
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
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

    public double getZoom() {
        return zoom;
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

    private void scrollTileToVisible(Point tile) {
        int twidth = map.getTileWidth();
        int theight = map.getTileHeight();

        scrollRectToVisible(new Rectangle(
                tile.x * twidth,
                tile.y * theight,
                twidth, theight));
    }

    @Override
    public abstract String getName();

    @Override
    public abstract Dimension getPreferredSize();

    /**
     * Tells this view a certain region of the map needs to be repainted.
     * <p>
     * Same as calling repaint() unless implemented more efficiently in a
     * subclass.
     *
     * @param region the region that has changed in tile coordinates
     */
    public void repaintRegion(Rectangle region) {
        repaint();
    }

    /**
     * Draws all the visible layers of the map. Takes several flags into
     * account when drawing, and will also draw the grid, and any 'special'
     * layers.
     *
     * @param g the Graphics2D object to paint to
     * @see javax.swing.JComponent#paintComponent(Graphics)
     * @see MapLayer
     * @see SelectionLayer
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Rectangle clip = g.getClipBounds();
        g2d.setClip(clip);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(DEFAULT_BACKGROUND_COLOR);
        g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
        paintMap(g2d);
        if (showGrid) {
            g2d.setComposite(AlphaComposite.SrcOver);
            g2d.setStroke(new BasicStroke());
            g2d.setColor(DEFAULT_GRID_COLOR);
            paintGrid(g2d);
        }
    }
    private AppData data = AppData.getInstance();
    private SelectionLayer cursorSelectionLayer = new SelectionLayer(1, 1); //当前选框
    private Rectangle marqueeSelection = new Rectangle();

    /**
     *
     * @return  
     */
    public SelectionLayer getCursorSelectionLayer() {
        return cursorSelectionLayer;
    }

    /**
     * 
     * @param g2d
     */
    public void paintSelection(Graphics2D g2d) {
        if (cursorSelectionLayer != null) {
            g2d.setColor(new Color(128, 128, 255));
            g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_ATOP, 0.75f));
            paintTileLayer(g2d, cursorSelectionLayer);
        }
    }

    /**
     *
     * 
     * @param g2d
     */
    public void paintMap(Graphics2D g2d) {

        Iterator li = map.getLayers();
        Layer layer;
        while (li.hasNext()) {
            layer = (Layer) li.next();
            if (layer != null) {
                if (layer.isIsVisible()) {
                    g2d.setComposite(AlphaComposite.SrcOver);
                    if (layer instanceof TileLayer) {
                        paintTileLayer(g2d, (TileLayer) layer);
                    } else if (layer instanceof ObjectLayer) {
                        paintObjectLayer(g2d, (ObjectLayer) layer);
                    }
                }
            }
        }
//        paintMarqueeSelection(g2d);
        paintSelection(g2d);//选框在最上层
    }

    /**
     * Draws a TileLayer. Implemented in a subclass.
     *
     * @param g2d   the graphics context to draw the layer onto
     * @param layer the TileLayer to be drawn
     */
    protected abstract void paintTileLayer(Graphics2D g2d, TileLayer layer);//画瓷砖层

    /**
     * 
     * @param g2d
     * @param layer
     */
    protected abstract void paintObjectLayer(Graphics2D g2d, ObjectLayer layer);//画对象层

    /**
     * Draws the map grid.
     *
     * @param g2d the graphics context to draw the grid onto
     */
    protected abstract void paintGrid(Graphics2D g2d);                  //画网格

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

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public abstract int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction);

    public abstract int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction);

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            setZoomLevel(zoomLevel + e.getWheelRotation());
            this.repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        Point tile = screenToTileCoords(e.getX(), e.getY());
        mouseButton = e.getButton();
        scrollTileToVisible(tile);
        //左键按下
        if (e.getButton() == MouseEvent.BUTTON1) {

            switch (data.currentPsType) {
                case AppData.PS_PEN:
                    if (data.getCurrentLayer() instanceof TileLayer) {
                        currentBrush.startPaint(map, tile.x, tile.y,
                                e.getButton(), map.indexOfLayer(data.getCurrentLayer()));
                        paintEdit =
                                new LayerEdit(data.getCurrentLayer(), createLayerCopy(data.getCurrentLayer()), null);
                    } else if (data.getCurrentLayer() instanceof ObjectLayer) {
                        MapObject object = new MapObject(
                                e.getX(),
                                e.getY(),
                                0,
                                0);
                        changeCurrentObject(object);
                        ObjectLayer group = (ObjectLayer) data.getCurrentLayer();
                        group.addObject(object);
                        marqueeSelection.setBounds(e.getX(), e.getY(), 0,
                                0);
                    }
                    break;
                case AppData.PS_ERASER:
                    paintEdit =
                            new LayerEdit(data.getCurrentLayer(), createLayerCopy(data.getCurrentLayer()), null);
                    break;
                case AppData.PS_FILL:
                    paintEdit =
                            new LayerEdit(data.getCurrentLayer(), createLayerCopy(data.getCurrentLayer()), null);
                case AppData.PS_MOVE:
                    changeCurrentObject(null);
                    break;
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (Configuration.getIsRightEraser()) {
                switch (data.currentPsType) {
                    case AppData.PS_PEN:
                    case AppData.PS_ERASER:
                        paintEdit =
                                new LayerEdit(data.getCurrentLayer(), createLayerCopy(data.getCurrentLayer()), null);
                        break;
                }
            }
        }
        mouse(e);
    }

    private void changeCurrentObject(MapObject object) {
        currentObject = object;
        data.getMainFrame().editObjectPane.setMapObject(object);
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
     * Returns the {@link UndoableEditSupport} instance.
     * @return the undo support
     */
    public UndoableEditSupport getUndoSupport() {
        return undoSupport;
    }

    /**
     * Returns the {@link UndoHandler} instance.
     * @return the undo stack
     */
    public UndoRedoHandler getUndoHandler() {
        return undoHandler;
    }

    public void mouseReleased(MouseEvent e) {
        switch (data.currentPsType) {
            case AppData.PS_PEN:
                if (mouseButton == MouseEvent.BUTTON1 && data.getCurrentLayer() instanceof TileLayer) {
                    currentBrush.endPaint();
                } else if (mouseButton == MouseEvent.BUTTON1 && data.getCurrentLayer() instanceof ObjectLayer) {
//                    Iterator<MapObject> itr = ((ObjectLayer) data.getCurrentLayer()).getObjects();
//                    ArrayList<MapObject> objs = new ArrayList<MapObject>();
//                    while (itr.hasNext()) {
//                        MapObject mo = itr.next();
//                        if (mo.getWidth() == 0 && mo.getHeight() == 0) {
//                            objs.add(mo);//对于宽高为0的对象，删除
//                        }
//                    }
//                    for(int i = 0;i<objs.size();i++){
//                        ((ObjectLayer) data.getCurrentLayer()).removeObject(objs.get(i));
//                    }
                    changeCurrentObject(null);
                }
                break;
//            case AppData.PS_ERASER:
//
//                break;
//            case AppData.PS_FILL:
//
//                break;
//            case AppData.PS_MOVE:
//
//                break;
        }

        repaint();
        repaintMiniMapImg();
        AppData.getInstance().miniMapRepaint();
//        data.miniMapShouldRepaint(true);
//        data.getMainFrame().miniMapPane.repaint();
        if (paintEdit != null) {
            if (data.getCurrentLayer() != null) {
                try {
                    Layer endLayer = paintEdit.getStart().createDiff(data.getCurrentLayer());
                    endLayer.setMap(map);
                    paintEdit.end(endLayer);
                    undoSupport.postEdit(paintEdit);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            paintEdit = null;
        }
    }

    private void paintMiniMap(Graphics2D g, Map map, double zoom) {
        Iterator li = map.getLayers();
        Layer layer;
        while (li.hasNext()) {
            layer = (Layer) li.next();
            if (layer != null) {
                if (layer.isIsVisible()) {
                    g.setComposite(AlphaComposite.SrcOver);
                    if (layer instanceof TileLayer) {
                        paintMiniTileLayer(g, (TileLayer) layer, zoom);
                    } else if (layer instanceof ObjectLayer) {
                        paintMiniObjectLayer(g, (ObjectLayer) layer, zoom);
                    }
                }
            }
        }
    }

    private void paintMiniTileLayer(Graphics2D g2d, TileLayer layer, double zoom) {
        for (int i = 0; i < layer.getHeight(); i++) {
            for (int j = 0; j < layer.getWidth(); j++) {
                MapTile tile = layer.getTileAt(j, i);
                if (tile != null) {
                    tile.draw(g2d, (int) (j * tile.getWidth() * zoom), (int) ((i + 1) * tile.getHeight() * zoom), zoom);
                }
            }
        }
    }

    private void paintMiniObjectLayer(Graphics2D g2d, ObjectLayer layer, double zoom) {
        Iterator<MapObject> itr = layer.getObjects();
        while (itr.hasNext()) {
            MapObject mo = itr.next();
            double ox = mo.getX() * zoom;
            double oy = mo.getY() * zoom;

            Image objectImage = mo.getImage(zoom);
            if (objectImage != null) {
                g2d.drawImage(objectImage, (int) ox, (int) oy, null);
            }
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.ORANGE);
            g2d.drawRect((int) ox + 1, (int) oy + 1,
                    (int) (mo.getWidth() * zoom),
                    (int) (mo.getHeight() * zoom));
            g2d.setColor(Color.BLUE);
            g2d.drawRect((int) ox, (int) oy,
                    (int) (mo.getWidth() * zoom),
                    (int) (mo.getHeight() * zoom));
        }
    }

    public void repaintMiniMapImg() {
        Graphics g2 = miniMapImg.getGraphics();
        Graphics2D g2d = (Graphics2D) g2.create();
        if (map != null) {
            float mapW = (map.getTileWidth() * map.getWidth());
            float mapH = (map.getTileHeight() * map.getHeight());
            float scale = 1.0f;//地图的横竖比
            double mzoom = 1.0f;
            g2d.setColor(MapRender.DEFAULT_BACKGROUND_COLOR);
            if (mapW >= mapH) {
                scale = mapH / mapW;
                mzoom = miniMapImg.getWidth() * 1.0 / (map.getTileWidth() * map.getWidth());
                g2d.fillRect(0, 0, miniMapImg.getWidth(), (int) (miniMapImg.getHeight() * scale));
            } else {
                scale = mapW / mapH;
                mzoom = miniMapImg.getHeight() * 1.0 / (map.getTileHeight() * map.getHeight());
                g2d.fillRect(0, 0, (int) (miniMapImg.getWidth() * scale), miniMapImg.getHeight());
            }
            paintMiniMap(g2d, map, mzoom);
        }

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        mouse(e);
        scrollTileToVisible(screenToTileCoords(e.getX(), e.getY()));
        Point tile = screenToTileCoords(e.getX(), e.getY());
        updateCursorHighlight(tile);
    }

    public void mouseMoved(MouseEvent e) {
        Point tile = screenToTileCoords(e.getX(), e.getY());
        if (e.getX() / map.getTileWidth() < map.getWidth() && e.getY() / map.getTileHeight() < map.getHeight()) {
            data.getMainFrame().stateLabel.setText(
                    " 当前位置: " + e.getY() / map.getTileHeight() + "行" + e.getX() / map.getTileWidth() + "列");
        }
        updateCursorHighlight(tile);
    }
    private int mouseButton;
    private Point mouseLastPixelLocation;
    protected MapObject currentObject = null;
    private Point moveDist;

    /**
     * 
     * @param e
     */
    public void mouse(MouseEvent e) {
        Point tile = screenToTileCoords(e.getX(), e.getY());
        if (mouseButton == MouseEvent.BUTTON3) {
            if (Configuration.getIsRightEraser()) {
                switch (data.currentPsType) {
                    case AppData.PS_PEN:
                    case AppData.PS_ERASER:
                        if (data.getCurrentLayer() instanceof TileLayer) {
                            paintEdit.setPresentationName("擦除");
                            ((TileLayer) data.getCurrentLayer()).setTileAt(tile.x, tile.y, null);
                            repaintRegion(new Rectangle(tile.x, tile.y, 1, 1));
                        } else if (data.getCurrentLayer() instanceof ObjectLayer) {
                            ObjectLayer group = (ObjectLayer) data.getCurrentLayer();
                            Point pos = screenToPixelCoords(
                                    e.getX(), e.getY());
                            MapObject obj = group.getObjectNear(pos.x, pos.y, getZoom());
                            if (obj != null) {
                                group.removeObject(obj);
                                changeCurrentObject(null);
                                // TODO: repaint only affected area
                                repaint();
                            }
                            repaintRegion(new Rectangle(tile.x, tile.y, 1, 1));
                        }
                        break;
                }
            }
        } else if (mouseButton == MouseEvent.BUTTON1) {
            switch (data.currentPsType) {
                case AppData.PS_PEN:
                    if (data.getCurrentLayer() instanceof TileLayer) {
                        if (currentBrush.isPaintingStarted()) {
                            paintEdit.setPresentationName("绘制");
                            try {
                                repaintRegion(
                                        currentBrush.doPaint(tile.x, tile.y));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else if (data.getCurrentLayer() instanceof ObjectLayer) {
                        if (currentObject != null) {
                            int x = marqueeSelection.x;
                            int y = marqueeSelection.y;
                            int w = Math.abs(x - e.getX());
                            int h = Math.abs(y - e.getY());
                            currentObject.setBounds(new Rectangle(Math.min(e.getX(), x), Math.min(e.getY(), y), w, h));
                            changeCurrentObject(currentObject);
                            repaint();
                        }
                    }
                    break;
                case AppData.PS_ERASER:
                    if (data.getCurrentLayer() instanceof TileLayer) {
                        paintEdit.setPresentationName("擦除");
                        ((TileLayer) data.getCurrentLayer()).setTileAt(tile.x, tile.y, null);
                        repaintRegion(new Rectangle(tile.x, tile.y, 1, 1));
                    } else if (data.getCurrentLayer() instanceof ObjectLayer) {
                        ObjectLayer group = (ObjectLayer) data.getCurrentLayer();
                        Point pos = screenToPixelCoords(
                                e.getX(), e.getY());
                        MapObject obj = group.getObjectNear(pos.x, pos.y, getZoom());
                        if (obj != null) {
                            group.removeObject(obj);
                            changeCurrentObject(null);
                            // TODO: repaint only affected area
                            repaint();
                        }
                        repaintRegion(new Rectangle(tile.x, tile.y, 1, 1));
                    }
                    break;
                case AppData.PS_MOVE:
                    if (data.getCurrentLayer() instanceof ObjectLayer) {
                        Point pos = screenToPixelCoords(
                                e.getX(), e.getY());
                        if (currentObject == null) {
                            ObjectLayer group = (ObjectLayer) data.getCurrentLayer();
                            changeCurrentObject(group.getObjectNear(pos.x, pos.y, getZoom()));
                            if (currentObject == null) { // No object to move
                                break;
                            }
                            mouseLastPixelLocation = pos;
                            moveDist = new Point(0, 0);
                            repaint();
                            break;
                        }
                        Point translation = new Point(
                                pos.x - mouseLastPixelLocation.x,
                                pos.y - mouseLastPixelLocation.y);
                        currentObject.translate(translation.x, translation.y);
                        changeCurrentObject(currentObject);
                        moveDist.translate(translation.x, translation.y);
                        mouseLastPixelLocation = pos;
                        repaint();
                    }
                    break;
                case AppData.PS_FILL:
                    if (data.getCurrentLayer() instanceof TileLayer) {
                        paintEdit.setPresentationName("填充");
                        TileLayer tileLayer = (TileLayer) data.getCurrentLayer();
                        MapTile oldTile = tileLayer.getTileAt(tile.x, tile.y);
                        pour(tileLayer, tile.x, tile.y, data.getCurrentTile(), oldTile);
                        repaint();
                    }
                    break;
            }
        }
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
    //扫描边种子填充算法

    private void pour(TileLayer layer, int x, int y, MapTile newTile, MapTile oldTile) {
        if (newTile == oldTile || !layer.isIsVisible()) {
            return;
        }
        Stack<Point> stack = new Stack<Point>();

        stack.push(new Point(x, y));
        while (!stack.empty()) {
            // Remove the next tile from the stack
            Point p = stack.pop();
            int px = p.x;
            int py = p.y;
            int savex = px;/* 保存种子坐标x分量的值 */

            //妙！！
            // If the tile it meets the requirements, set it and push its
            // neighbouring tiles on the stack.
//            if (layer.contains(p.x, p.y) && layer.getTileAt(p.x, p.y) == oldTile) {
//                layer.setTileAt(p.x, p.y, newTile);
////                area.add(p);
//
//                stack.push(new Point(p.x, p.y - 1));
//                stack.push(new Point(p.x, p.y + 1));
//                stack.push(new Point(p.x + 1, p.y));
//                stack.push(new Point(p.x - 1, p.y));
//            }
            int xleft = 0, xright = 0;
            for (; layer.contains(px, py) && layer.getTileAt(px, py) == oldTile; px++) {
                layer.setTileAt(px, py, newTile);
            }
            xright = px - 1;/* 得到种子区段的右端点 */
            px = savex - 1;/* 准备向种子左侧填充 */
            for (; layer.contains(px, py) && layer.getTileAt(px, py) == oldTile; px--) {
                layer.setTileAt(px, py, newTile);
            }
            xleft = px + 1; /* 得到种子区段的左端点 */
            px = xleft;
            py = py - 1; /* 考虑种子相邻的上扫描线 */
            for (; px <= xright; px++) {
                if (layer.contains(px, py) && layer.getTileAt(px, py) == oldTile) {
                    stack.push(new Point(px, py));
                    break;
                }
            }
            px = xleft;
            for (; px <= xright; px++) {
                if (layer.contains(px, py) && layer.getTileAt(px - 1, py) != oldTile && layer.getTileAt(px, py) == oldTile) {
                    stack.push(new Point(px, py));
                }
            }
            px = xleft;
            py = py + 2;/* 检查相邻的下扫描线 */
            for (; px <= xright; px++) {
                if (layer.contains(px, py) && layer.getTileAt(px, py) == oldTile) {
                    stack.push(new Point(px, py));
                    break;
                }

            }
            px = xleft;
            for (; px <= xright; px++) {
                if (layer.contains(px, py) && layer.getTileAt(px - 1, py) != oldTile && layer.getTileAt(px, py) == oldTile) {
                    stack.push(new Point(px, py));
                }
            }
        }
    }

    private void updateCursorHighlight(Point tile) {

        Rectangle redraw = cursorSelectionLayer.getBounds();
        Rectangle brushRedraw = currentBrush.getBounds();

        brushRedraw.x = tile.x - brushRedraw.width / 2;
        brushRedraw.y = tile.y - brushRedraw.height / 2;

        if (!redraw.equals(brushRedraw)) {
            if (currentBrush instanceof CustomBrush) {
                CustomBrush customBrush = (CustomBrush) currentBrush;
                ListIterator<Layer> layers = customBrush.getListLayers();
                while (layers.hasNext()) {
                    Layer layer = (Layer) layers.next();
                    layer.setOffset(brushRedraw.x, brushRedraw.y);
                }
            }
            repaintRegion(redraw);
            cursorSelectionLayer.setOffset(brushRedraw.x, brushRedraw.y);
            repaintRegion(brushRedraw);
        }
    }
}
