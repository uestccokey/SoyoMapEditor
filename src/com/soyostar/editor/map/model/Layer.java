/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.model;

import com.soyostar.editor.map.listener.LayerChangeListener;
import com.soyostar.editor.map.listener.LayerChangedEvent;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public abstract class Layer implements Cloneable {

    /**
     * 
     */
    public static final byte TILELAYER = 0;
    /**
     * 
     */
    public static final byte COLLIDELAYER = 1;
    /**
     * 
     */
    public static final byte OBJECTLAYER = 2;
    /**
     * 
     */
    protected String name;
    /**
     * 
     */
    protected Rectangle bounds;
    /**
     * 
     */
    protected boolean isVisible = true;
    /**
     * 
     */
    protected Map map;
    /**
     * 
     */
    protected final List<LayerChangeListener> layerChangeListeners = new LinkedList();
    private HashMap<String, String> propertys = new HashMap<String, String>();

    /**
     *
     * @param map
     */
    public Layer(Map map) {
        this();
        setMap(map);
    }

    /**
     * @param w width in tiles
     * @param h height in tiles
     */
    public Layer(int w, int h) {
        this(new Rectangle(0, 0, w, h));
    }

    /**
     *
     */
    public Layer() {
        bounds = new Rectangle();
        setMap(null);
    }

    /**
     * @param m the map this layer is part of
     * @param w width in tiles
     * @param h height in tiles
     */
    public Layer(Map m, int w, int h) {
        this(w, h);
        setMap(m);
    }

    /**
     *
     * @param r
     */
    public Layer(Rectangle r) {
        this();
        setBounds(r);
    }

    /**
     * Creates a copy of this layer.
     *
     * @see Object#clone
     * @return a clone of this layer, as complete as possible
     * @exception CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Layer clone = (Layer) super.clone();
        // Create a new bounds object
        clone.bounds = new Rectangle(bounds);
        return clone;
    }

    public HashMap<String, String> getProperties() {
        return propertys;
    }

    public String getProperty(String key) {
        return propertys.get(key);
    }

    public void addProperty(String key, String value) {
        propertys.put(key, value);
        fireLayerChanged();
    }

    public void removeProperty(String key) {
        propertys.remove(key);
        fireLayerChanged();
    }

    public void removeAllProperty() {
        propertys.clear();
        fireLayerChanged();
    }

    /**
     * Sets the offset of this map layer. The offset is a distance by which to
     * shift this layer from the origin of the map.
     *
     * @param xOff x offset in tiles
     * @param yOff y offset in tiles
     */
    public void setOffset(int xOff, int yOff) {
        bounds.x = xOff;
        bounds.y = yOff;
    }

    /**
     * 
     * @param listener
     */
    public void addLayerChangeListener(LayerChangeListener listener) {
        layerChangeListeners.add(listener);
    }

    /**
     * 
     * @param listener
     */
    public void removeLayerChangeListener(LayerChangeListener listener) {
        layerChangeListeners.remove(listener);
    }

    /**
     * 
     */
    protected void fireLayerChanged() {
        LayerChangedEvent event = new LayerChangedEvent(this);
        for (LayerChangeListener listener : layerChangeListeners) {
            listener.layerChanged(event);
        }
    }

    /**
     * 
     * @param oldName
     * @param newName
     */
    protected void fireNameChanged(String oldName, String newName) {
        LayerChangedEvent event = new LayerChangedEvent(this);
        for (LayerChangeListener listener : layerChangeListeners) {
            listener.nameChanged(event, oldName, newName);
        }
    }

    /**
     * 
     * @param newBool
     */
    protected void fireVisibleChanged(boolean newBool) {
        LayerChangedEvent event = new LayerChangedEvent(this);
        for (LayerChangeListener listener : layerChangeListeners) {
            listener.visibleChanged(event, newBool);
        }
    }

    @Override
    public String toString() {
        return "*Layer* name:" + this.getName()
                + "\n      width:" + this.getWidth()
                + "\n      height:" + this.getHeight();
    }

    /**
     * Returns layer width in tiles.
     * @return layer width in tiles.
     */
    public int getWidth() {
        return bounds.width;
    }

    /**
     * Returns layer height in tiles.
     * @return layer height in tiles.
     */
    public int getHeight() {
        return bounds.height;
    }

    /**
     * Sets the bounds (in tiles) to the specified Rectangle.
     *
     * @param bounds
     */
    protected void setBounds(Rectangle bounds) {
        this.bounds = new Rectangle(bounds);
        fireLayerChanged();
    }

    /**
     * 
     * @return
     */
    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

    /**
     * Assigns the layer bounds in tiles to the given rectangle.
     * @param rect the rectangle to which the layer bounds are assigned
     */
    public void getBounds(Rectangle rect) {
        rect.setBounds(bounds);
    }

    /**
     * A convenience method to check if a point in tile-space is within
     * the layer boundaries.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return <code>true</code> if the point (x,y) is within the layer
     *         boundaries, <code>false</code> otherwise.
     */
    public boolean contains(int x, int y) {
        return bounds.contains(x, y);
    }

    /**
     *
     * @return
     */
    public boolean isIsVisible() {
        return isVisible;
    }

    /**
     *
     * @param isVisible
     */
    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
        fireVisibleChanged(isVisible);
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
     * @param myMap
     */
    public void setMap(Map myMap) {
        this.map = myMap;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        String na = this.name;
        this.name = name;
        fireNameChanged(na, name);
    }

    /**
     * Merges the tile data of this layer with the specified layer. The calling
     * layer is considered the significant layer, and will overwrite the data
     * of the argument layer. At cells where the calling layer has no data, the
     * argument layer data is preserved.
     *
     * @param other the insignificant layer to merge with
     */
    public abstract void mergeOnto(Layer other);

    /**
     * 
     * @param ml
     * @return
     */
    public abstract Layer createDiff(Layer ml);

    /**
     * Unlike mergeOnto, copyTo includes the null tile when merging
     *
     * @see MapLayer#copyFrom
     * @see MapLayer#mergeOnto
     * @param other the layer to copy this layer to
     */
    public abstract void copyTo(Layer other);

    /**
     * 
     * @param other
     */
    public abstract void copyFrom(Layer other);

    /**
     *
     * @param w
     * @param h
     * @param dx
     * @param dy
     */
    public abstract void resize(int w, int h, int dx, int dy);
}
