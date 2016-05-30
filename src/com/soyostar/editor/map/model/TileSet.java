/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.model;

import com.soyostar.editor.map.listener.TilesetChangeListener;
import com.soyostar.editor.map.listener.TilesetChangedEvent;
import com.soyostar.editor.util.SloppyArray;
import com.soyostar.editor.util.TileCutter;
import java.awt.*;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class TileSet {

    private int gid;                    //全局id，用于区分自身tile
    private SloppyArray tiles, images;  //瓷砖包，图像包
    private String name;                //图集名
    private BufferedImage tileSetImage;         //图集图像
    private String tilebmpFile;         //图集文件名
    private Map map;                    //所属地图
    private final List<TilesetChangeListener> tilesetChangeListeners = new LinkedList();

    /**
     * Default constructor
     */
    public TileSet() {
        tiles = new SloppyArray();
        images = new SloppyArray();
        tileDimensions = new Rectangle();
    }

    /**
     * 
     * @return
     */
    public int getIndex() {
        return map.getTileSets().indexOf(this);
    }

    /**
     *
     * @return
     */
    public int getFirstGid() {
        return gid;
    }

    /**
     *
     * @param id
     */
    public void setFirstGid(int id) {
        this.gid = id;
    }

    /**
     * 
     * @param listener
     */
    public void addTilesetChangeListener(TilesetChangeListener listener) {
        tilesetChangeListeners.add(listener);
    }

    /**
     * 
     * @param listener
     */
    public void removeTilesetChangeListener(TilesetChangeListener listener) {
        tilesetChangeListeners.remove(listener);
    }

    private void fireTilesetChanged() {
        TilesetChangedEvent event = new TilesetChangedEvent(this);
        for (TilesetChangeListener listener : tilesetChangeListeners) {
            listener.tilesetChanged(event);
        }
    }

    private void fireNameChanged(String oldName, String newName) {
        TilesetChangedEvent event = new TilesetChangedEvent(this);
        for (TilesetChangeListener listener : tilesetChangeListeners) {
            listener.nameChanged(event, oldName, newName);
        }
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
    }

    /**
     *
     * @return
     */
    public String getTilebmpFile() {
        return tilebmpFile;
    }

    /**
     *
     * @param tilebmpFile
     */
    public void setTilebmpFile(String tilebmpFile) {
        int temp = tilebmpFile.lastIndexOf("\\");
        this.tilebmpFile = tilebmpFile.substring(temp + 1);
    }
    private int tilesPerRow;
    private Rectangle tileDimensions;

    /**
     *
     * @param id
     * @return
     */
    public Dimension getImageDimensions(int id) {
        Image img = (Image) images.get(id);
        if (img != null) {
            return new Dimension(img.getWidth(null), img.getHeight(null));
        } else {
            return new Dimension(0, 0);
        }
    }

    @Override
    public String toString() {
        return "*TileSet* name:" + this.getName()
                + "\n          file:" + this.getTilebmpFile()
                + "\n          size:" + this.size()
                + "\n          tileW:" + this.getTileWidth()
                + "\n          tileH:" + this.getTileHeight()
                + "\n          width:" + this.getWidth()
                + "\n          height:" + this.getHeight()
                + "\n          id:" + this.getIndex();
    }

    /**
     * Creates a tileset from a tileset image file.
     *
     * @param imgFilename
     * @param cutter
     * @throws IOException
     * @see TileSet#importTileBitmap(BufferedImage, TileCutter)
     */
    public void importTileBitmap(String imgFilename, TileCutter cutter)
            throws IOException {
        setTilebmpFile(imgFilename);
        Image image = ImageIO.read(new File(imgFilename));
        if (image == null) {
            throw new IOException("Failed to load " + tilebmpFile);
        }

        BufferedImage buffered = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(image, 0, 0, null);
        importTileBitmap(buffered, cutter);
    }

    /**
     * Generates a vector that removes the gaps that can occur if a tile is
     * removed from the middle of a set of tiles. (Maps tiles contiguously)
     *
     * @return a {@link Vector} mapping ordered set location to the next
     *         non-null tile
     */
    public ArrayList<MapTile> generateGaplessArrayList() {
        ArrayList<MapTile> gapless = new ArrayList<MapTile>();

        for (int i = 0; i <= getMaxTileId(); i++) {
            if (getTile(i) != null) {
                gapless.add(getTile(i));
            }
        }

        return gapless;
    }

    /**
     * Creates a tileset from a buffered image. Tiles are cut by the passed
     * cutter.
     *
     * @param tilebmp     the image to be used, must not be null
     * @param cutter      the tile cutter, must not be null
     */
    private void importTileBitmap(BufferedImage tilebmp, TileCutter cutter) {
        assert tilebmp != null;
        assert cutter != null;

        tileSetImage = tilebmp;
        cutter.setImage(tilebmp);
        tileDimensions = new Rectangle(cutter.getTileDimensions());
        tilesPerRow = cutter.getTilesPerRow();
        Image tile = cutter.getNextTile();
        while (tile != null) {
            MapTile newTile = new MapTile();
            newTile.setImage(addImage(tile));
            addNewTile(newTile);
            tile = cutter.getNextTile();
        }
    }

    /**
     * Adds the tile to the set, setting the id of the tile only if the current
     * value of id is -1.
     *
     * @param t the tile to add
     * @return int The <b>local</b> id of the tile
     */
    public int addTile(MapTile t) {
        if (t.getIndex() < 0) {
            t.setIndex(tiles.getMaxId() + 1);
        }
        tiles.put(t.getIndex(), t);
        t.setTileSet(this);
        fireTilesetChanged();
        return t.getIndex();
    }

    /**
     * This method takes a new Tile object as argument, and in addition to
     * the functionality of <code>addTile()</code>, sets the id of the tile
     * to -1.
     *
     * @see TileSet#addTile(Tile)
     * @param t the new tile to add.
     */
    public void addNewTile(MapTile t) {
        t.setIndex(-1);
        addTile(t);
    }

    /**
     * Removes a tile from this tileset. Does not invalidate other tile
     * indices. Removal is simply setting the reference at the specified
     * index to <b>null</b>.
     *
     * todo: Fix the behaviour of this function? It actually does seem to
     * todo: invalidate other tile indices due to implementation of
     * todo: NumberedSet.
     *
     * @param i the index to remove
     */
    public void removeTile(int i) {
        tiles.remove(i);
        fireTilesetChanged();
    }

    /**
     * Sets the name of this tileset.
     *
     * @param name the new name for this tileset
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        fireNameChanged(oldName, name);
    }

    /**
     * @return the name of this tileset.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an iterator over the tiles in this tileset.
     *
     * @return an iterator over the tiles in this tileset.
     */
    public Iterator iterator() {
        return tiles.iterator();
    }

    /**
     * Returns the amount of tiles in this tileset.
     *
     * @return the amount of tiles in this tileset
     */
    public int size() {
        return tiles.size();
    }

    /**
     * Returns the maximum tile id.
     *
     * @return the maximum tile id, or -1 when there are no tiles
     */
    public int getMaxTileId() {
        return tiles.getMaxId();
    }

    /**
     * Gets the tile with <b>local</b> id <code>i</code>.
     *
     * @param i local id of tile
     * @return A tile with local id <code>i</code> or <code>null</code> if no
     *         tile exists with that id
     */
    public MapTile getTile(int i) {
        try {
            return (MapTile) tiles.get(i);
        } catch (ArrayIndexOutOfBoundsException a) {
        }
        return null;
    }

    /**
     * Returns the first non-null tile in the set.
     *
     * @return The first tile in this tileset, or <code>null</code> if none
     *         exists.
     */
    public MapTile getFirstTile() {
        MapTile ret = null;
        int i = 0;
        while (ret == null && i <= getMaxTileId()) {
            ret = getTile(i);
            i++;
        }
        return ret;
    }

    /**
     * Returns the number of images in the set.
     *
     * @return the number of images in the set
     */
    public int getTotalImages() {
        return images.size();
    }

    // TILE IMAGE CODE
    /**
     * This function uses the CRC32 checksums to find the cached version of the
     * image supplied.
     *
     * @param i an Image object
     * @return returns the id of the given image, or -1 if the image is not in
     *         the set
     */
    public int getIdByImage(Image i) {
        return images.indexOf(i);
    }

    /**
     * @param id
     * @return the image identified by the key, or <code>null</code> when
     *         there is no such image
     */
    public Image getImageById(int id) {
        return (Image) images.get(id);
    }

    /**
     * Overlays the image in the set referred to by the given key.
     *
     * @param id
     * @param image
     */
    public void overlayImage(int id, Image image) {
        images.put(id, image);
    }

    /**
     * Adds the specified image to the image cache. If the image already exists
     * in the cache, returns the id of the existing image. If it does not
     * exist, this function adds the image and returns the new id.
     *
     * @param image the java.awt.Image to add to the image cache
     * @return the id as an <code>int</code> of the image in the cache
     */
    public int addImage(Image image) {
        return images.findOrAdd(image);
    }

    /**
     *
     * @param image
     * @param id
     * @return
     */
    public int addImage(Image image, int id) {
        return images.put(id, image);
    }

    /**
     *
     * @param id
     */
    public void removeImage(int id) {
        images.remove(id);
    }

    /**
     *
     * @return
     */
    public int getWidth() {
        return tileSetImage.getWidth(null);
    }

    /**
     *
     * @return
     */
    public int getHeight() {
        return tileSetImage.getHeight(null);
    }

    /**
     *
     * @return
     */
    public BufferedImage getTileSetImage() {
        return tileSetImage;
    }

    /**
     * Returns the width of tiles in this tileset. All tiles in a tileset
     * should be the same width, and the same as the tile width of the map the
     * tileset is used with.
     *
     * @return int - The maximum tile width
     */
    public int getTileWidth() {
        return tileDimensions.width;
    }

    /**
     * Returns the tile height of tiles in this tileset. Not all tiles in a
     * tileset are required to have the same height, but the height should be
     * at least the tile height of the map the tileset is used with.
     *
     * If there are tiles with varying heights in this tileset, the returned
     * height will be the maximum.
     *
     * @return the max height of the tiles in the set
     */
    public int getTileHeight() {
        return tileDimensions.height;
    }

    /**
     * Returns the number of tiles per row in the original tileset image.
     * @return the number of tiles per row in the original tileset image.
     */
    public int getTilesPerRow() {
        return tilesPerRow;
    }
}
