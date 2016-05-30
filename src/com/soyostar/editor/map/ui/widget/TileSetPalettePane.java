/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.ui.widget;

import com.soyostar.editor.map.listener.TileRegionSelectionEvent;
import com.soyostar.editor.map.listener.TileSelectionEvent;
import com.soyostar.editor.map.listener.TileSelectionListener;
import com.soyostar.editor.map.model.TileLayer;
import com.soyostar.editor.map.model.MapTile;
import com.soyostar.editor.map.model.TileSet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author 图元调色板，每张图都有一个调色板
 */
public class TileSetPalettePane extends JPanel implements Scrollable {
    
    private TileSet tileset;
    private ArrayList<MapTile> tilesetMap;
    private Rectangle selection;
    private List<TileSelectionListener> tileSelectionListeners;

    /**
     *
     */
    public TileSetPalettePane() {
        tileSelectionListeners = new LinkedList();
        
        MouseInputAdapter mouseInputAdapter = new MouseInputAdapter() {
            
            private Point origin;
            
            @Override
            public void mousePressed(MouseEvent e) {
                origin = getTileCoordinates(e.getX(), e.getY());
                setSelection(new Rectangle(origin.x, origin.y, 0, 0));
                scrollTileToVisible(origin);
                MapTile clickedTile = getTileAt(origin.x, origin.y);
                if (clickedTile != null) {
                    fireTileSelectionEvent(clickedTile);
//                    System.out.println("*****************************");
//                    System.out.println("TileID:" + clickedTile.getId());
//                    System.out.println("*****************************");
                }
//                Point point = getTileCoordinates(e.getX(), e.getY());
//                Rectangle select = new Rectangle(origin.x, origin.y, 0, 0);
//                scrollTileToVisible(origin);
//                select.add(point);
//                if (!select.equals(selection)) {
//                setSelection(select);
//                    scrollTileToVisible(point);
//                }
//                if (selection.getWidth() > 0 || selection.getHeight() > 0) {
//                    fireTileRegionSelectionEvent(selection);
//                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = getTileCoordinates(e.getX(), e.getY());
                Rectangle select = new Rectangle(origin.x, origin.y, 0, 0);
                select.add(point);
                if (!select.equals(selection)) {
                    setSelection(select);
                    scrollTileToVisible(point);
                }
//                Tile[] coverTiles = getTilesAt(select);
//                if (coverTiles != null) {
//                    System.out.println("*****************************");
//                    for (int i = 0; i < coverTiles.length; i++) {
//                        System.out.println("TileID:" + coverTiles[i].getId());
//                    }
//                    System.out.println("*****************************");
//                }
                if (selection.getWidth() > 0 || selection.getHeight() > 0) {
                    fireTileRegionSelectionEvent(selection);
                }
            }
        };
        addMouseListener(mouseInputAdapter);
        addMouseMotionListener(mouseInputAdapter);
    }

    /**
     * Adds tile selection listener. The listener will be notified when the
     * user selects a tile.
     *
     * @param listener the listener to add
     */
    public void addTileSelectionListener(TileSelectionListener listener) {
        tileSelectionListeners.add(listener);
    }

    /**
     * Removes tile selection listener.
     *
     * @param listener the listener to remove
     */
    public void removeTileSelectionListener(TileSelectionListener listener) {
        tileSelectionListeners.remove(listener);
    }
    
    private void fireTileSelectionEvent(MapTile selectedTile) {
        TileSelectionEvent event = new TileSelectionEvent(this, selectedTile);
        for (TileSelectionListener listener : tileSelectionListeners) {
            listener.tileSelected(event);
        }
    }
    
    private void fireTileRegionSelectionEvent(Rectangle selection) {
        TileLayer region = createTileLayerFromRegion(selection);
        TileRegionSelectionEvent event = new TileRegionSelectionEvent(this, region);
        for (TileSelectionListener listener : tileSelectionListeners) {
            listener.tileRegionSelected(event);
        }
    }
    //拖动时，根据鼠标位置可以自动滚动！

    private void scrollTileToVisible(Point tile) {
        int twidth = tileset.getTileWidth();
        int theight = tileset.getTileHeight();
        
        scrollRectToVisible(new Rectangle(
            tile.x * twidth,
            tile.y * theight,
            twidth, theight));
    }

    /**
     * Converts pixel coordinates to tile coordinates. The returned coordinates
     * are at least 0 and adjusted with respect to the number of tiles per row
     * and the number of rows.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return tile coordinates
     */
    private Point getTileCoordinates(int x, int y) {
        int twidth = tileset.getTileWidth();
        int theight = tileset.getTileHeight();
        int tileCount = tilesetMap.size();
        int tilesPerRow = getTilesPerRow();
        int rows = tileCount / tilesPerRow
            + (tileCount % tilesPerRow > 0 ? 1 : 0);
        
        int tileX = Math.max(0, Math.min(x / twidth, tilesPerRow - 1));
        int tileY = Math.max(0, Math.min(y / theight, rows - 1));
        
        return new Point(tileX, tileY);
    }

    /**
     * Returns the number of tiles to display per row. This gets calculated
     * dynamically unless the tileset specifies this value.
     *
     * @return the number of tiles to display per row, is at least 1
     */
    private int getTilesPerRow() {
        // todo: It should be an option to follow the tiles per row given
        // todo: by the tileset.
        return tileset.getTilesPerRow();
    }

    /**
     *
     * @param tileSet
     */
    public void setTileSet(TileSet tileSet) {
        this.tileset = tileSet;
        if (tileset != null) {
            tilesetMap = tileset.generateGaplessArrayList();
        }
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBackground(g);
        if (tileset != null) {
            // Draw the tiles
            g.drawImage(tileset.getTileSetImage(), 0, 0, null);
        }
        // Draw the selection
        if (selection != null) {
            g.setColor(new Color(100, 100, 255));
            g.draw3DRect(
                selection.x * tileset.getTileWidth(), selection.y * tileset.getTileHeight(),
                (selection.width + 1) * tileset.getTileWidth(),
                (selection.height + 1) * tileset.getTileHeight(),
                false);
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_ATOP, 0.2f));
            g.fillRect(
                selection.x * tileset.getTileWidth() + 1, selection.y * tileset.getTileHeight() + 1,
                (selection.width + 1) * tileset.getTileWidth() - 1,
                (selection.height + 1) * tileset.getTileHeight() - 1);
        }
    }

    /**
     * Draws checkerboard background.
     *
     * @param g the {@link Graphics} instance to draw on
     */
    private static void paintBackground(Graphics g) {
        Rectangle clip = g.getClipBounds();
        int side = 8;
        
        int startX = clip.x / side;
        int startY = clip.y / side;
        int endX = (clip.x + clip.width) / side + 1;
        int endY = (clip.y + clip.height) / side + 1;

        // Fill with white background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        // Draw darker squares
        g.setColor(Color.LIGHT_GRAY);
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                if ((y + x) % 2 == 1) {
                    g.fillRect(x * side, y * side, side, side);
                }
            }
        }
    }
    
    private void repaintSelection() {
        if (selection != null) {
            int twidth = tileset.getTileWidth();
            int theight = tileset.getTileHeight();
            
            repaint(selection.x * twidth - 1, selection.y * theight - 1,
                (selection.width + 1) * twidth + 2,
                (selection.height + 1) * theight + 2);//重绘区域稍大于选框，避免留下选框边缘
        }
    }
    
    private void setSelection(Rectangle rect) {
        repaintSelection();
        selection = rect;
        repaintSelection();
    }

    /**
     *
     * @return
     */
    public TileSet getTileset() {
        return tileset;
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (tileset != null) {
            int twidth = tileset.getTileWidth();
            int theight = tileset.getTileHeight();
            int tileCount = tilesetMap.size();
            int tilesPerRow = getTilesPerRow();
            int rows = tileCount / tilesPerRow
                + (tileCount % tilesPerRow > 0 ? 1 : 0);
            
            return new Dimension(tilesPerRow * twidth + 1, rows * theight + 1);
        }
        return new Dimension(0, 0);
    }

    /**
     * Retrieves the tile at the given tile coordinates. It assumes the tile
     * coordinates are adjusted to the number of tiles per row.
     *
     * @param x x tile coordinate
     * @param y y tile coordinate
     * @return the tile at the given tile coordinates, or <code>null</code>
     *         if the index is out of range
     */
    private MapTile getTileAt(int x, int y) {
        int tilesPerRow = getTilesPerRow();
        int tileAt = y * tilesPerRow + x;
        if (tileAt >= tilesetMap.size()) {
            return null;
        } else {
            return tilesetMap.get(tileAt);
        }
    }

    /**
     * Creates a tile layer from a certain region of the tile palette.
     *
     * @param rect the rectangular region from which a tile layer is created
     * @return the created tile layer
     */
    private TileLayer createTileLayerFromRegion(Rectangle rect) {
        TileLayer layer = new TileLayer(rect.width + 1, rect.height + 1);
//        layer.setMap(null);
        // Copy the tiles in the region to the tile layer
        for (int y = rect.y; y <= rect.y + rect.height; y++) {
            for (int x = rect.x; x <= rect.x + rect.width; x++) {
                layer.setTileAt(x - rect.x, y - rect.y, getTileAt(x, y));
            }
        }
        
        return layer;
    }
    
    private MapTile[] getTilesAt(Rectangle coverRect) {
        ArrayList<MapTile> tiles = new ArrayList<MapTile>();
        int tilesPerRow = getTilesPerRow();
        int tileAt = coverRect.y * tilesPerRow + coverRect.x;
//        System.out.println("tileAt:" + tileAt);
        int tileWn = coverRect.width + 1;
//        System.out.println("tileWn:" + tileWn);
        int tileHn = coverRect.height + 1;
//        System.out.println("tileHn:" + tileHn);
        for (int j = 0; j < tileHn; j++) {
            for (int i = tileAt; i < tileWn + tileAt; i++) {
                tiles.add(tilesetMap.get(i + j * getTilesPerRow()));
            }
        }
        return tiles.toArray(new MapTile[tiles.size()]);
    }
    //调节滚动快慢

    public int getScrollableUnitIncrement(Rectangle visibleRect,
        int orientation, int direction) {
        if (tileset != null) {
            return tileset.getTileHeight();
        } else {
            return 0;
        }
    }
    
    public int getScrollableBlockIncrement(Rectangle visibleRect,
        int orientation, int direction) {
        if (tileset != null) {
            return tileset.getTileHeight();
        } else {
            return 0;
        }
    }
    
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
    
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    public Dimension getPreferredScrollableViewportSize() {
        return null;
    }
}
