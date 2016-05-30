/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.model;

import java.awt.*;

/**
 *
 * @author Administrator
 */
public class TileLayer extends Layer {

    /**
     * 
     */
    protected MapTile[][] tiles;

    /**
     * Default contructor.
     */
    public TileLayer() {
    }

    /**
     * Construct a TileLayer from the given width and height.
     *
     * @param w width in tiles
     * @param h height in tiles
     */
    public TileLayer(int w, int h) {
        super(w, h);
    }

    /**
     * Create a tile layer using the given bounds.
     *
     * @param r the bounds of the tile layer.
     */
    public TileLayer(Rectangle r) {
        super(r);
    }

    /**
     * @param m the map this layer is part of
     */
    TileLayer(Map m) {
        super(m);
    }

    /**
     * @param m the map this layer is part of
     * @param w width in tiles
     * @param h height in tiles
     */
    public TileLayer(Map m, int w, int h) {
        super(w, h);
        setMap(m);
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
        TileLayer clone = (TileLayer) super.clone();
        // Clone the layer data
        clone.tiles = new MapTile[tiles.length][];
        for (int i = 0, n = tiles.length; i < n; i++) {
            clone.tiles[i] = new MapTile[tiles[i].length];
            System.arraycopy(tiles[i], 0, clone.tiles[i], 0, tiles[i].length);
        }
        return clone;
    }

    /**
     * Creates a diff of the two layers, <code>ml</code> is considered the
     * significant difference.
     *
     * @param ml
     * @return A new MapLayer that represents the difference between this
     *         layer, and the argument, or <b>null</b> if no difference exists.
     */
    public Layer createDiff(Layer ml) {
        if (ml == null) {
            return null;
        }

        if (ml instanceof TileLayer) {
            Rectangle r = null;

            for (int y = bounds.y; y < bounds.height + bounds.y; y++) {
                for (int x = bounds.x; x < bounds.width + bounds.x; x++) {
                    if (((TileLayer) ml).getTileAt(x, y) != getTileAt(x, y)) {
                        if (r != null) {
                            r.add(x, y);
                        } else {
                            r = new Rectangle(new Point(x, y));
                        }
                    }
                }
            }

            if (r != null) {
                TileLayer diff = new TileLayer(
                        new Rectangle(r.x, r.y, r.width + 1, r.height + 1));
                diff.setMap(map);
                diff.copyFrom(ml);
                return diff;
            } else {
                return new TileLayer(map);
            }
        } else {
            return null;
        }
    }

    /**
     * Copy data from another layer onto this layer. Unlike mergeOnto,
     * copyFrom() copies the empty cells as well.
     *
     * @see MapLayer#mergeOnto
     * @param other
     */
    public void copyFrom(Layer other) {
        if (!isIsVisible()) {
            return;
        }

        for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
            for (int x = bounds.x; x < bounds.x + bounds.width; x++) {
                setTileAt(x, y, ((TileLayer) other).getTileAt(x, y));
            }
        }
    }

    /**
     * Sets the bounds (in tiles) to the specified Rectangle.
     *
     * @param bounds
     */
    @Override
    protected void setBounds(Rectangle bounds) {
        this.bounds = new Rectangle(bounds);
        tiles = new MapTile[bounds.height][bounds.width];
        fireLayerChanged();
    }

    /**
     * Removes any occurences of the given tile from this map layer. If layer
     * is locked, an exception is thrown.
     *
     * @param tile the Tile to be removed
     * @throws Exception
     */
    public void removeTile(MapTile tile) throws Exception {
        if (!isIsVisible()) {
            throw new Exception(
                    "图层不可视！");
        }

        for (int y = 0; y < bounds.height; y++) {
            for (int x = 0; x < bounds.width; x++) {
                if (tiles[y][x] == tile) {
                    setTileAt(x + bounds.x, y + bounds.y, null);
                }
            }
        }
    }

    /**
     * Sets the tile at the specified position. Does nothing if (tx, ty) falls
     * outside of this layer.
     *
     * @param tx x position of tile
     * @param ty y position of tile
     * @param ti the tile object to place
     */
    public void setTileAt(int tx, int ty, MapTile ti) {
        if (bounds.contains(tx, ty) && isIsVisible()) {
            tiles[ty - bounds.y][tx - bounds.x] = ti;
            fireLayerChanged();
        }
    }

    /**
     * Returns the tile at the specified position.
     *
     * @param tx Tile-space x coordinate
     * @param ty Tile-space y coordinate
     * @return tile at position (tx, ty) or <code>null</code> when (tx, ty) is
     *         outside this layer
     */
    public MapTile getTileAt(int tx, int ty) {
        MapTile tile = null;
        if (bounds.contains(tx, ty)) {
            return tiles[ty - bounds.y][tx - bounds.x];
        }
        return tile;
    }

    /**
     * Returns the first occurance (using top down, left to right search) of
     * the given tile.
     *
     * @param t the {@link Tile} to look for
     * @return A java.awt.Point instance of the first instance of t, or
     *         <code>null</code> if it is not found
     */
    public Point locationOf(MapTile t) {
        for (int y = bounds.y; y < bounds.height + bounds.y; y++) {
            for (int x = bounds.x; x < bounds.width + bounds.x; x++) {
                if (getTileAt(x, y) == t) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Replaces all occurances of the Tile <code>find</code> with the Tile
     * <code>replace</code> in the entire layer
     *
     * @param find    the tile to replace
     * @param replace the replacement tile
     * @throws Exception
     */
    public void replaceTile(MapTile find, MapTile replace) throws Exception {
        if (!isIsVisible()) {
            throw new Exception(
                    "图层不可视！");
        }

        for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
            for (int x = bounds.x; x < bounds.x + bounds.width; x++) {
                if (getTileAt(x, y) == find) {
                    setTileAt(x, y, replace);
                }
            }
        }
    }

    /**
     * Checks to see if the given Tile is used anywhere in the layer.
     *
     * @param t a Tile object to check for
     * @return <code>true</code> if the Tile is used at least once,
     *         <code>false</code> otherwise.
     */
    public boolean isUsed(MapTile t) {
        for (int y = 0; y < bounds.height; y++) {
            for (int x = 0; x < bounds.width; x++) {
                if (tiles[y][x] == t) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        for (int p = 0; p < 2; p++) {
            for (int y = 0; y < bounds.height; y++) {
                for (int x = p; x < bounds.width; x += 2) {
                    if (tiles[y][x] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param other
     * @inheritDoc MapLayer#mergeOnto(MapLayer)
     */
    public void mergeOnto(Layer other) {
        if (!other.isIsVisible()) {
            return;
        }
        if (other instanceof TileLayer) {
            for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
                for (int x = bounds.x; x < bounds.x + bounds.width; x++) {
                    MapTile tile = getTileAt(x, y);
                    if (tile != null) {
                        ((TileLayer) other).setTileAt(x, y, tile);
                    }
                }
            }
        }

    }

    /**
     * @see MultilayerPlane#resize
     *
     * @param width  the new width of the layer
     * @param height the new height of the layer
     * @param dx     the shift in x direction
     * @param dy     the shift in y direction
     */
    public void resize(int width, int height, int dx, int dy) {
//        if (!isIsVisible()) {
//            return;
//        }

        MapTile[][] newMapt = new MapTile[height][width];
        int maxX = Math.min(width, bounds.width + dx);
        int maxY = Math.min(height, bounds.height + dy);

        for (int x = Math.max(0, dx); x < maxX; x++) {
            for (int y = Math.max(0, dy); y < maxY; y++) {
                newMapt[y][x] = getTileAt(x - dx, y - dy);
            }
        }
        tiles = newMapt;
        bounds.width = width;
        bounds.height = height;
    }

    /**
     * Unlike mergeOnto, copyTo includes the null tile when merging.
     *
     * @see MapLayer#copyFrom
     * @see MapLayer#mergeOnto
     * @param other the layer to copy this layer to
     */
    public void copyTo(Layer other) {
        if (!other.isIsVisible()) {
            return;
        }
        for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
            for (int x = bounds.x; x < bounds.x + bounds.width; x++) {
                ((TileLayer) other).setTileAt(x, y, getTileAt(x, y));
            }
        }
    }
}
