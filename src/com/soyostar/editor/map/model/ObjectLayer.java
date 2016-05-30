/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.model;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ObjectLayer extends Layer {

    private List<MapObject> objects = new LinkedList<MapObject>();

    /**
     * Default constructor.
     */
    public ObjectLayer() {
    }

    /**
     * @param map    the map this object group is part of
     */
    public ObjectLayer(Map map) {
        super(map);
    }

    /**
     * Creates an object group that is part of the given map and has the given
     * origin.
     *
     * @param map    the map this object group is part of
     * @param origx  the x origin of this layer
     * @param origy  the y origin of this layer
     */
    public ObjectLayer(Map map, int origx, int origy) {
        super(map);
        setBounds(new Rectangle(origx, origy, 0, 0));
    }

    /**
     * Creates an object group with a given area. The size of area is
     * irrelevant, just its origin.
     *
     * @param area the area of the object group
     */
    public ObjectLayer(Rectangle area) {
        super(area);
    }

    @Override
    public void mergeOnto(Layer other) {
    }

    @Override
    public Layer createDiff(Layer ml) {
        return ml;
    }

    @Override
    public void copyTo(Layer other) {
    }

    @Override
    public void copyFrom(Layer other) {
    }

    @Override
    public void resize(int w, int h, int dx, int dy) {
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    public void addObject(MapObject o) {
        objects.add(o);
        o.setObjectLayer(this);
    }

    public int getObjectCount() {
        return objects.size();
    }

    public void removeObject(MapObject o) {
        objects.remove(o);
        o.setObjectLayer(null);
    }

    public MapObject getObjectAt(int x, int y) {
        for (MapObject obj : objects) {
            // Attempt to get an object bordering the point that has no width
            if (obj.getWidth() == 0 && obj.getX() + bounds.x == x) {
                return obj;
            }

            // Attempt to get an object bordering the point that has no height
            if (obj.getHeight() == 0 && obj.getY() + bounds.y == y) {
                return obj;
            }

            Rectangle rect = new Rectangle(obj.getX() + bounds.x * map.getTileWidth(),
                    obj.getY() + bounds.y * map.getTileHeight(),
                    obj.getWidth(), obj.getHeight());
            if (rect.contains(x, y)) {
                return obj;
            }
        }
        return null;
    }

    public Iterator<MapObject> getObjects() {
        return objects.iterator();
    }

    // This method will work at any zoom level, provided you provide the correct zoom factor. It also adds a one pixel buffer (that doesn't change with zoom).
    public MapObject getObjectNear(int x, int y, double zoom) {
        Rectangle2D mouse = new Rectangle2D.Double(x - zoom - 1, y - zoom - 1, 2 * zoom + 1, 2 * zoom + 1);
        Shape shape;

        for (MapObject obj : objects) {
            if (obj.getWidth() == 0 && obj.getHeight() == 0) {
                shape = new Ellipse2D.Double(obj.getX() * zoom, obj.getY() * zoom, 10 * zoom, 10 * zoom);
            } else {
                shape = new Rectangle2D.Double(obj.getX() + bounds.x * map.getTileWidth(),
                        obj.getY() + bounds.y * map.getTileHeight(),
                        obj.getWidth() > 0 ? obj.getWidth() : zoom,
                        obj.getHeight() > 0 ? obj.getHeight() : zoom);
            }

            if (shape.intersects(mouse)) {
                return obj;
            }
        }

        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ObjectLayer clone = (ObjectLayer) super.clone();
        clone.objects = new LinkedList<MapObject>();
        for (MapObject object : objects) {
            final MapObject objectClone = (MapObject) object.clone();
            clone.objects.add(objectClone);
            objectClone.setObjectLayer(clone);
        }
        return clone;
    }
}
