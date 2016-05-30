/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.ui.render;

import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.MapObject;
import com.soyostar.editor.map.model.ObjectLayer;
import com.soyostar.editor.map.model.TileLayer;
import com.soyostar.editor.map.model.SelectionLayer;
import com.soyostar.editor.map.model.MapTile;
import java.awt.*;
import java.util.Iterator;
import javax.swing.SwingConstants;

/**
 *
 * @author Administrator
 */
public class OrthoMapRender extends MapRender {

    /**
     *
     * @param map
     */
    public OrthoMapRender(Map map) {
        super(map);
    }

    /**
     *
     */
    public OrthoMapRender() {
        super();
    }

    public String getName() {
        return "正视角";
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        Dimension tsize = getTileSize();

        if (orientation == SwingConstants.VERTICAL) {
            return (visibleRect.height / tsize.height) * tsize.height;
        } else {
            return (visibleRect.width / tsize.width) * tsize.width;
        }
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        Dimension tsize = getTileSize();
        if (orientation == SwingConstants.VERTICAL) {
            return tsize.height;
        } else {
            return tsize.width;
        }
    }

    public Dimension getPreferredSize() {
        Dimension tsize = getTileSize();

        return new Dimension(
                map.getWidth() * tsize.width,
                map.getHeight() * tsize.height);
    }

    @Override
    protected void paintGrid(Graphics2D g2d) {
        Dimension tsize = getTileSize();
        if (tsize.width <= 0 || tsize.height <= 0) {
            return;
        }
        // Determine lines to draw from clipping rectangle
        Rectangle clipRect = g2d.getClipBounds();
        int startX = clipRect.x / tsize.width * tsize.width;
        int startY = clipRect.y / tsize.height * tsize.height;
        int endX = clipRect.x + clipRect.width;
        int endY = clipRect.y + clipRect.height;
        for (int x = startX; x < endX; x += tsize.width) {
            g2d.drawLine(x, clipRect.y, x, clipRect.y + clipRect.height);
        }
        for (int y = startY; y < endY; y += tsize.height) {
            g2d.drawLine(clipRect.x, y, clipRect.x + clipRect.width, y);
        }
    }

    /**
     *
     * @param tx
     * @param ty
     * @param border
     * @return
     */
    protected Polygon createGridPolygon(int tx, int ty, int border) {
        Dimension tsize = getTileSize();

        Polygon poly = new Polygon();
        poly.addPoint(tx - border, ty - border);
        poly.addPoint(tx + tsize.width + border, ty - border);
        poly.addPoint(tx + tsize.width + border, ty + tsize.height + border);
        poly.addPoint(tx - border, ty + tsize.height + border);

        return poly;
    }

    protected void paintTileLayer(Graphics2D g2d, TileLayer layer) {
        // Determine tile size and offset
        Dimension tsize = getTileSize();
        if (tsize.width <= 0 || tsize.height <= 0) {
            return;
        }
        Polygon gridPoly = createGridPolygon(0, -tsize.height, 0);

        // Determine area to draw from clipping rectangle
        Rectangle clipRect = g2d.getClipBounds();
        int startX = clipRect.x / tsize.width;
        int startY = clipRect.y / tsize.height;
        int endX = (clipRect.x + clipRect.width) / tsize.width + 1;
        int endY = (clipRect.y + clipRect.height) / tsize.height + 1;
        // (endY +2 for high tiles, could be done more properly)

        // Draw this map layer
        for (int y = startY, gy = (startY + 1) * tsize.height;
                y < endY; y++, gy += tsize.height) {
            for (int x = startX, gx = startX * tsize.width;
                    x < endX; x++, gx += tsize.width) {
                MapTile tile = layer.getTileAt(x, y);
                if (tile != null) {
                    if (layer instanceof SelectionLayer) {
                        gridPoly.translate(gx, gy);
                        g2d.fillPolygon(gridPoly);
                        tile.draw(g2d, gx, gy, zoom);
                        gridPoly.translate(-gx, -gy);
                    } else {
                        tile.draw(g2d, gx, gy, zoom);
                    }
                }
            }
        }
    }

    @Override
    public void repaintRegion(Rectangle region) {
        Dimension tsize = getTileSize();
        if (tsize.width <= 0 || tsize.height <= 0) {
            return;
        }

        // Calculate the visible corners of the region
        int startX = region.x * tsize.width;
        int startY = region.y * tsize.height;
        int endX = (region.x + region.width) * tsize.width;
        int endY = (region.y + region.height) * tsize.height;

        Rectangle dirty =
                new Rectangle(startX, startY, endX - startX, endY - startY);

        repaint(dirty);
    }

    public Point tileToScreenCoords(int x, int y) {
        Dimension tsize = getTileSize();
        return new Point(x * tsize.width, y * tsize.height);
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Point screenToTileCoords(int x, int y) {
        Dimension tsize = getTileSize();
        return new Point(x / tsize.width, y / tsize.height);
    }

    public Point screenToPixelCoords(int x, int y) {
        return new Point(
                (int) (x / zoom), (int) (y / zoom));
    }

    /**
     * 
     * @param g2d
     * @param layer
     */
    @Override
    protected void paintObjectLayer(Graphics2D g2d, ObjectLayer layer) {
        final Dimension tsize = getTileSize();
        final Rectangle bounds = layer.getBounds();
        Iterator<MapObject> itr = layer.getObjects();
        g2d.translate(
                bounds.x * tsize.width,
                bounds.y * tsize.height);

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
//            if (mo.getWidth() == 0 || mo.getHeight() == 0) {
//                g2d.setRenderingHint(
//                        RenderingHints.KEY_ANTIALIASING,
//                        RenderingHints.VALUE_ANTIALIAS_ON);
//                g2d.setColor(Color.black);
//                g2d.fillOval((int) ox + 1, (int) oy + 1,
//                        (int) (10 * zoom), (int) (10 * zoom));
//                g2d.setColor(Color.orange);
//                g2d.fillOval((int) ox, (int) oy,
//                        (int) (10 * zoom), (int) (10 * zoom));
//                g2d.setRenderingHint(
//                        RenderingHints.KEY_ANTIALIASING,
//                        RenderingHints.VALUE_ANTIALIAS_OFF);
//            } else {
            g2d.setColor(Color.ORANGE);
            g2d.drawRoundRect((int) ox + 1, (int) oy + 1,
                    (int) (mo.getWidth() * zoom),
                    (int) (mo.getHeight() * zoom), 8, 8);
//            g2d.drawRect((int) ox + 1, (int) oy + 1,
//                    (int) (mo.getWidth() * zoom),
//                    (int) (mo.getHeight() * zoom));
            if (currentObject == mo) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLUE);
            }
            g2d.drawRoundRect((int) ox, (int) oy,
                    (int) (mo.getWidth() * zoom),
                    (int) (mo.getHeight() * zoom), 8, 8);
//            g2d.drawRect((int) ox, (int) oy,
//                    (int) (mo.getWidth() * zoom),
//                    (int) (mo.getHeight() * zoom));
//            }
//            if (zoom > 0.0625) {
//                final String s = mo.getName() != null ? mo.getName() : "(null)";
//                g2d.setColor(Color.black);
//                g2d.drawString(s, (int) (ox - 5) + 1, (int) (oy - 5) + 1);
//                g2d.setColor(Color.white);
//                g2d.drawString(s, (int) (ox - 5), (int) (oy - 5));
//            }
        }

        g2d.translate(
                -bounds.x * tsize.width,
                -bounds.y * tsize.height);
    }
}
