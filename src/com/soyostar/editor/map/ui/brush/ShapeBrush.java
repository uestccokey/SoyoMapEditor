/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.ui.brush;

import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.TileLayer;
import com.soyostar.editor.map.model.MapTile;
import java.awt.*;
import java.awt.geom.*;

/**
 * @version $Id$
 */
public class ShapeBrush extends AbBrush {

    /**
     *
     */
    protected Area shape;
    /**
     *
     */
    protected MapTile paintTile;

    /**
     *
     */
    public ShapeBrush() {
    }

    /**
     *
     * @param shape
     */
    public ShapeBrush(Area shape) {
        this.shape = shape;
    }

    /**
     *
     * @param sb
     */
    public ShapeBrush(AbBrush sb) {
        super(sb);
        if (sb instanceof ShapeBrush) {
            shape = ((ShapeBrush) sb).shape;
            paintTile = ((ShapeBrush) sb).paintTile;
        }
    }

    /**
     * Makes this brush a circular brush.
     *
     * @param rad the radius of the circular region
     */
    public void makeCircleBrush(double rad) {
        shape = new Area(new Ellipse2D.Double(0, 0, rad * 2, rad * 2));
        resize((int) (rad * 2), (int) (rad * 2), 0, 0);
    }

    /**
     * Makes this brush a rectangular brush.
     *
     * @param r a Rectangle to use as the shape of the brush
     */
    public void makeQuadBrush(Rectangle r) {
        shape = new Area(new Rectangle2D.Double(r.x, r.y, r.width, r.height));
        resize(r.width, r.height, 0, 0);
    }

    /**
     *
     * @param p
     */
    public void makePolygonBrush(Polygon p) {
    }

    /**
     *
     * @param size
     */
    public void setSize(int size) {
        if (shape.isRectangular()) {
            makeQuadBrush(new Rectangle(0, 0, size, size));
        } else if (!shape.isPolygonal()) {
            makeCircleBrush(size / 2);
        } else {
            // TODO: scale the polygon brush
        }
    }

    /**
     *
     * @param t
     */
    public void setTile(MapTile t) {
        paintTile = t;
    }

    /**
     *
     * @return
     */
    public MapTile getTile() {
        return paintTile;
    }

    @Override
    public Rectangle getBounds() {
        return shape.getBounds();
    }

    /**
     *
     * @return
     */
    public Shape getShape() {
        return shape;
    }

    public boolean equals(IBrush brush) {
        return brush instanceof ShapeBrush
                && ((ShapeBrush) brush).shape.equals(shape);
    }

    @Override
    public void startPaint(Map mp, int x, int y, int button, int layer) {
        super.startPaint(mp, x, y, button, layer);
    }

    /**
     * Paints the entire area of the brush with the set tile. This brush can
     * affect several layers.
     * @throws Exception
     *
     * @see tiled.mapeditor.brush.Brush#doPaint(int, int)
     */
    @Override
    public Rectangle doPaint(int x, int y) throws Exception {
        Rectangle shapeBounds = shape.getBounds();
        int centerx = x - shapeBounds.width / 2;
        int centery = y - shapeBounds.height / 2;

        super.doPaint(x, y);
        if (affectedMp.getLayer(initLayer) instanceof TileLayer) {
            TileLayer tl = (TileLayer) affectedMp.getLayer(initLayer);
            if (tl != null) {
                for (int i = 0; i <= shapeBounds.height + 1; i++) {
                    for (int j = 0; j <= shapeBounds.width + 1; j++) {
                        if (shape.contains(j, i)) {
                            tl.setTileAt(j + centerx, i + centery, paintTile);
                        }
                    }
                }
            }
        }

        // Return affected area
        return new Rectangle(
                centerx, centery, shapeBounds.width, shapeBounds.height);
    }
}
