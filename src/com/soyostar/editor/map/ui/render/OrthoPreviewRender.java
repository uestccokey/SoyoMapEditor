/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.ui.render;

import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.ObjectLayer;
import com.soyostar.editor.map.model.MapTile;
import com.soyostar.editor.map.model.TileLayer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;

/**
 *
 * @author Administrator
 */
public class OrthoPreviewRender extends PreviewRender {

    /**
     * 
     * @param map
     */
    public OrthoPreviewRender(Map map) {
        super(map);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension tsize = getTileSize();
        return new Dimension(
                map.getWidth() * tsize.width,
                map.getHeight() * tsize.height);
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

    @Override
    protected void paintTileLayer(Graphics2D g2d, TileLayer layer) {

        Dimension tsize = getTileSize();
        if (tsize.width <= 0 || tsize.height <= 0) {
            return;
        }
        int startX = 0;
        int startY = 0;
        int endX = this.getWidth() / tsize.width + 1;
        int endY = this.getHeight() / tsize.height + 1;

        // Draw this map layer
        for (int y = startY, gy = (startY + 1) * tsize.height;
                y < endY; y++, gy += tsize.height) {
            for (int x = startX, gx = startX * tsize.width;
                    x < endX; x++, gx += tsize.width) {
                MapTile tile = layer.getTileAt(x, y);
                if (tile != null) {
                    tile.draw(g2d, gx, gy, zoom);
                }
            }
        }
    }

    @Override
    protected void paintObjectLayer(Graphics2D g2d, ObjectLayer layer) {
    }

    @Override
    public Point screenToTileCoords(int x, int y) {
        Dimension tsize = getTileSize();
        return new Point(x / tsize.width, y / tsize.height);
    }

    @Override
    public Point screenToPixelCoords(int x, int y) {
        return new Point(
                (int) (x / zoom), (int) (y / zoom));
    }

    @Override
    public Point tileToScreenCoords(int x, int y) {
        Dimension tsize = getTileSize();
        return new Point(x * tsize.width, y * tsize.height);
    }
}
