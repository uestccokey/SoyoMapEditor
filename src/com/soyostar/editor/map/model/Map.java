/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.model;

import com.soyostar.editor.map.listener.MapChangeListener;
import com.soyostar.editor.map.listener.MapChangedEvent;
import com.soyostar.editor.map.ui.render.MapRender;
import com.soyostar.editor.map.ui.render.PreviewRender;
import java.util.*;
import java.awt.Rectangle;

/**
 *
 * @author Administrator
 */
public class Map {

    private static final String ORTHOGONAL = "orthogonal";
    private static final String ISOMETRIC = "isometric";
    private HashMap<String, String> properties = new HashMap<String, String>();  //属性
    private ArrayList<Layer> layers;            //支持多层地图
    private ArrayList<TileSet> tilesets;        //支持多图元
    private HashMap<String, String> mapTypeMap = new HashMap<String, String>();
    private String mapType = "正视角";       //默认地图类型
    private String name;
    private int id = -1;
    private MapRender mapRender;                //每个地图都有自己的绘制器
    private PreviewRender previewRender;        //地图的预览窗口
    private int cellWidth, cellHeight;          //瓷砖大小
    private final List mapChangeListeners = new LinkedList();
    /**
     * 行数
     */
    protected int rowNum;
    /**
     * 列数
     */
    protected int colNum;

    /**
     * 
     */
    public Map() {
        mapTypeMap.put("正视角", ORTHOGONAL);
        mapTypeMap.put("45度斜视角", ISOMETRIC);
        tilesets = new ArrayList<TileSet>();
        layers = new ArrayList<Layer>();
    }
    //通过地图类型，返回地图的视角

    public String getMapOrientation() {
        return mapTypeMap.get(mapType);
    }

    public void setMapOrientation(String str) {
        if (mapTypeMap.containsKey(str)) {
            mapType = mapTypeMap.get(str);
        }
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
        fireMapChanged();
    }

    public void removeProperty(String key) {
        properties.remove(key);
        fireMapChanged();
    }

    public void removeAllProperty() {
        properties.clear();
        fireMapChanged();
    }

    /**
     * Get the tile set that matches the given global tile id, only to be used
     * when loading a map.
     *
     * @param gid a global tile id
     * @return the tileset containing the tile with the given global tile id,
     *         or <code>null</code> when no such tileset exists
     */
    public TileSet findTileSetForTileGID(int gid) {
        TileSet has = null;
        for (TileSet tileset : tilesets) {
            if (tileset.getFirstGid() <= gid) {
                has = tileset;
            }
        }
        return has;
    }

    /**
     * 
     * @return
     */
    public int getIndex() {
        return id;
    }

    /**
     * 
     * @param id
     */
    public void setIndex(int id) {
        this.id = id;
        fireMapChanged();
    }

    /**
     * 
     * @param index
     * @return
     */
    public Layer getLayer(int index) {
        if (index < 0 || index >= layers.size()) {
            return null;
        }
        return (Layer) layers.get(index);
    }

    /**
     * 
     * @return
     */
    public PreviewRender getPreviewRender() {
        return previewRender;
    }

    /**
     * 
     * @param previewRender
     */
    public void setPreviewRender(PreviewRender previewRender) {
        this.previewRender = previewRender;
    }

    /**
     * Adds a change listener. The listener will be notified when the map
     * changes in certain ways.
     *
     * @param listener the change listener to add
     * @see MapChangeListener#mapChanged(MapChangedEvent)
     */
    public void addMapChangeListener(MapChangeListener listener) {
        mapChangeListeners.add(listener);
    }

    /**
     * Removes a change listener.
     * @param listener the listener to remove
     */
    public void removeMapChangeListener(MapChangeListener listener) {
        mapChangeListeners.remove(listener);
    }

    /**
     * Notifies all registered map change listeners about a change.
     */
    protected void fireMapChanged() {
        Iterator iterator = mapChangeListeners.iterator();
        MapChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new MapChangedEvent(this);
            }
            ((MapChangeListener) iterator.next()).mapChanged(event);
        }
    }

    /**
     * Notifies all registered map change listeners about the removal of a
     * tileset.
     *
     * @param index the index of the removed tileset
     */
    protected void fireLayerRemoved(int index) {
        Iterator iterator = mapChangeListeners.iterator();
        MapChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new MapChangedEvent(this);
            }
            ((MapChangeListener) iterator.next()).layerRemoved(event, index);
        }
    }

    /**
     * Notifies all registered map change listeners about the removal of a
     * tileset.
     *
     * @param index the index of the removed tileset
     */
    protected void fireTilesetRemoved(int index) {
        Iterator iterator = mapChangeListeners.iterator();
        MapChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new MapChangedEvent(this);
            }

            ((MapChangeListener) iterator.next()).tilesetRemoved(event, index);
        }
    }

    /**
     * Notifies all registered map change listeners about the addition of a
     * tileset.
     *
     * @param layer the new layer
     */
    protected void fireLayerAdded(Layer layer) {
        Iterator iterator = mapChangeListeners.iterator();
        MapChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new MapChangedEvent(this);
            }
            ((MapChangeListener) iterator.next()).layerAdded(event, layer);
        }
    }

    /**
     * Notifies all registered map change listeners about the addition of a
     * tileset.
     *
     * @param tileset the new tileset
     */
    protected void fireTilesetAdded(TileSet tileset) {
        Iterator iterator = mapChangeListeners.iterator();
        MapChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new MapChangedEvent(this);
            }
            ((MapChangeListener) iterator.next()).tilesetAdded(event, tileset);
        }
    }

    /**
     * Moves the layer at <code>index</code> up one in the vector.
     *
     * @param index the index of the layer to swap up
     */
    public void swapLayerDown(int index) {
        if (index + 1 == layers.size()) {
            throw new RuntimeException(
                    "Can't swap up when already at the top.");
        }
        Layer hold = getLayer(index + 1);
        layers.set(index + 1, getLayer(index));
        layers.set(index, hold);
        fireMapChanged();
    }

    /**
     * Moves the layer at <code>index</code> down one in the vector.
     *
     * @param index the index of the layer to swap down
     */
    public void swapLayerUp(int index) {
        if (index - 1 < 0) {
            throw new RuntimeException(
                    "Can't swap down when already at the bottom.");
        }
        Layer hold = getLayer(index - 1);
        layers.set(index - 1, getLayer(index));
        layers.set(index, hold);
        fireMapChanged();
    }
//    @Override
//    public String toString() {
//        return "*Map* name:" + this.getName()
//            + "\n      Width:" + this.getWidth()
//            + "\n      Height:" + this.getHeight()
//            + "\n      TileWidth:" + this.getTileWidth()
//            + "\n      TileHeight:" + this.getTileHeight();
//    }

    @Override
    public String toString() {
        return this.getName()
                + " (ID:" + this.getIndex()
                + ",宽:" + this.getWidth()
                + ",高:" + this.getHeight() + ")";
    }

    /**
     *
     * @return
     */
    public MapRender getMapRender() {
        return mapRender;
    }

    /**
     *
     * @param mapView
     */
    public void setMapRender(MapRender mapView) {
        this.mapRender = mapView;
    }

    /**
     *
     * @param orientation
     */
    public void setMapType(String orientation) {
        this.mapType = orientation;
    }

    /**
     *
     * @return
     */
    public String getMapType() {
        return mapType;
    }

    /**
     * Returns a <code>Rectangle</code> representing the maximum bounds in
     * tiles.
     * @return a new rectangle containing the maximum bounds of this plane
     */
    public Rectangle getBounds() {
        return new Rectangle(colNum, rowNum);
    }

    /**
     * Returns the maximum tile height. This is the height of the highest tile
     * in all tilesets or the tile height used by this map if it's smaller.
     *
     * @return int The maximum tile height
     */
    public int getTileHeightMax() {
        int maxHeight = cellHeight;

        for (TileSet tileset : tilesets) {
            int height = ((TileSet) tileset).getTileHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }
        }

        return maxHeight;
    }

    /**
     * Returns wether the given tile coordinates fall within the map
     * boundaries.
     *
     * @param x The tile-space x-coordinate
     * @param y The tile-space y-coordinate
     * @return <code>true</code> if the point is within the map boundaries,
     *         <code>false</code> otherwise
     */
    public boolean contains(int x, int y) {

        return !(x < 0 || y < 0 || x >= colNum || y >= rowNum);
//        return x >= 0 && y >= 0 && x < colNum && y < rowNum;
    }

    /**
     * Returns the total number of layers.
     *
     * @return the size of the layer vector
     */
    public int getTotalLayers() {
        return layers.size();
    }

    /**
     * Returns the total number of layers.
     *
     * @return the size of the layer vector
     */
    public int getTotalTileLayers() {
        int n = 0;
        for (int i = 0; i < layers.size(); i++) {
            if (this.getLayer(i) instanceof TileLayer) {
                n++;
            }
        }
        return n;
    }

    /**
     * Returns the total number of layers.
     *
     * @return the size of the layer vector
     */
    public int getTotalObjectLayers() {
        int n = 0;
        for (int i = 0; i < layers.size(); i++) {
            if (this.getLayer(i) instanceof ObjectLayer) {
                n++;
            }
        }
        return n;
    }

    /**
     * Add a layer at the specified index, which should be within
     * the valid range.
     *
     * @param index the position at which to add the layer
     * @param layer the layer to add
     */
    public void addLayer(int index, Layer layer) {
        layer.setMap(this);
        layers.add(index, layer);
        fireLayerAdded(layer);
    }

    /**
     * Returns the layer vector.
     *
     * @return Vector the layer vector
     */
    public ArrayList<Layer> getLayerArrayList() {
        return layers;
    }

    /**
     * Sets the layer vector to the given java.util.Vector.
     *
     * @param layers the new set of layers
     */
    public void setLayerArrayList(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    /**
     *
     * @param tileHeight
     */
    public void setTileHeight(int tileHeight) {
        this.cellHeight = tileHeight;
        fireMapChanged();
    }

    /**
     *
     * @return
     */
    public int getTileHeight() {
        return cellHeight;
    }

    /**
     *
     * @param tileWidth
     */
    public void setTileWidth(int tileWidth) {
        this.cellWidth = tileWidth;
        fireMapChanged();
    }

    /**
     *
     * @return
     */
    public int getTileWidth() {
        return cellWidth;
    }

    /**
     *
     * @param height
     */
    public void setHeight(int height) {
        this.rowNum = height;
        fireMapChanged();
    }

    /**
     * Returns height of map in tiles.
     *
     * @return int
     */
    public int getHeight() {
        return rowNum;
    }

    /**
     *
     * @param width
     */
    public void setWidth(int width) {
        this.colNum = width;
        fireMapChanged();
    }

    /**
     * Returns width of map in tiles.
     *
     * @return int
     */
    public int getWidth() {
        return colNum;
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
        this.name = name;
        fireMapChanged();
    }

    /**
     * 
     * @return
     */
    public int getMaxTileSetIndex() {
        int max = -1;
        Iterator it = tilesets.iterator();
        while (it.hasNext()) {
            TileSet m = (TileSet) it.next();
            if (m.getIndex() > max) {
                max = m.getIndex();
            }
        }
        return max;
    }

    public TileSet getTileSet(int index) {
        if (index < 0 || index >= tilesets.size()) {
            return null;
        }
        return tilesets.get(index);
    }

    /**
     * Adds a Tileset to this Map. If the set is already attached to this map,
     * <code>addTileset</code> simply returns.
     *
     * @param tileset a tileset to add
     */
    public void addTileset(TileSet tileset) {
        if (tileset == null || tilesets.indexOf(tileset) > -1) {
            return;
        }

        MapTile t = tileset.getTile(0);

        if (t != null) {
            int tw = t.getWidth();
            int th = t.getHeight();
            if (tw != cellWidth) {
                if (cellWidth == 0) {
                    cellWidth = tw;
                    cellHeight = th;
                }
            }
        }
        tileset.setMap(this);
        tilesets.add(tileset);
        fireTilesetAdded(tileset);
    }

    /**
     * Removes a {@link TileSet} from the map, and removes any tiles in the set
     * from the map layers. A {@link MapChangedEvent} is fired when all
     * processing is complete.
     *
     * @param tileset TileSet to remove
     * @throws Exception  
     */
    public void removeTileset(TileSet tileset) throws Exception {
        // Sanity check
        final int tilesetIndex = tilesets.indexOf(tileset);
        if (tilesetIndex == -1) {
            return;
        }
        // Go through the map and remove any instances of the tiles in the set
        Iterator tileIterator = tileset.iterator();
        while (tileIterator.hasNext()) {
            MapTile tile = (MapTile) tileIterator.next();
            Iterator layerIterator = getLayers();
            while (layerIterator.hasNext()) {
                Layer ml = (Layer) layerIterator.next();
                if (ml instanceof TileLayer) {
                    ((TileLayer) ml).removeTile(tile);
                }
            }
        }
        tilesets.remove(tileset);
        fireTilesetRemoved(tilesetIndex);
    }

    /**
     *
     * @param layer
     */
    public void addLayer(Layer layer) {
        layer.setMap(this);
        layers.add(layer);
        fireLayerAdded(layer);
    }

    /**
     *
     * @param index
     * @param layer
     */
    public void setLayer(int index, TileLayer layer) {
        layer.setMap(this);
        layers.set(index, layer);
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @param index
     * @see MultilayerPlane#removeLayer(int)
     */
    public void removeLayer(int index) {
        layers.remove(index);
        fireLayerRemoved(index);
    }

    /**
     * Calls super method, and additionally fires a {@link MapChangedEvent}.
     *
     * @param layer
     * @see MultilayerPlane#removeLayer(int)
     */
    public void removeLayer(Layer layer) {
        int index = layers.indexOf(layer);
        if (index != -1) {
            layers.remove(index);
            fireLayerRemoved(index);
        } else {
            System.out.println("删除图层失败:错误的图层！");
        }
    }

    /**
     *
     */
    public void removeAllLayers() {
        layers.clear();
        fireMapChanged();
    }

    /**
     * Adds all the layers in a given java.util.Collection.
     *
     * @param layers a collection of layers to add
     */
    public void addAllLayers(Collection<Layer> layers) {
        this.layers.addAll(layers);
        fireMapChanged();
    }

    /**
     * Gets a listIterator of all layers.
     *
     * @return a listIterator
     */
    public ListIterator<Layer> getListLayers() {
        return layers.listIterator();
    }

    /**
     *
     * @return
     */
    public Iterator getLayers() {
        return layers.iterator();
    }

    /**
     * Resizes this plane. The (dx, dy) pair determines where the original
     * plane should be positioned on the new area. Only layers that exactly
     * match the bounds of the map are resized, any other layers are moved by
     * the given shift.
     *
     * @see MapLayer#resize
     *
     * @param width  The new width of the map.
     * @param height The new height of the map.
     * @param dx     The shift in x direction in tiles.
     * @param dy     The shift in y direction in tiles.
     */
    public void resize(int width, int height, int dx, int dy) {
        ListIterator itr = getListLayers();
        while (itr.hasNext()) {
            Layer layer = (Layer) itr.next();
            if (layer.bounds.equals(new Rectangle(colNum, rowNum))) {
                layer.resize(width, height, dx, dy);
            } else {
                layer.setOffset(layer.bounds.x + dx, layer.bounds.y + dy);
            }
        }

        colNum = width;
        rowNum = height;
        fireMapChanged();
    }

    /**
     * 
     * @param layer
     * @return
     */
    public int indexOfLayer(Layer layer) {
        return layers.indexOf(layer);
    }

    /**
     * Create a new empty TileLayer with the dimensions of the map. By default,
     * the new layer's name is set to "Layer [layer index]"
     *
     * @return The new TileLayer instance.
     */
    public ObjectLayer addObjectLayer() {
        ObjectLayer layer = new ObjectLayer(this, colNum, rowNum);
        int n = 0;
        for (int i = 0; i < getTotalLayers(); i++) {
            if (this.getLayer(i) instanceof ObjectLayer) {
                n++;
            }
        }
        layer.setName("对象层"
                + n);
//        layer.setMap(this);//初始化此layer时，已经设置了map
        layers.add(layer);
        fireLayerAdded(layer);
        return layer;
    }

    /**
     * Create a new empty TileLayer with the dimensions of the map. By default,
     * the new layer's name is set to "Layer [layer index]"
     *
     * @return The new TileLayer instance.
     */
    public TileLayer addTileLayer() {
        TileLayer layer = new TileLayer(this, colNum, rowNum);
        int n = 0;
        for (int i = 0; i < getTotalLayers(); i++) {
            if (this.getLayer(i) instanceof TileLayer) {
                n++;
            }
        }
        layer.setName("瓷砖层"
                + n);
//        layer.setMap(this);//初始化此layer时，已经设置了map
        layers.add(layer);
        fireLayerAdded(layer);
        return layer;
    }

    /**
     * Changes the bounds of this plane to include all layers completely.
     */
    public void fitBoundsToLayers() {
        int width = 0;
        int height = 0;

        Rectangle layerBounds = new Rectangle();

        for (int i = layers.size() - 1; i >= 0; i--) {
            getLayer(i).getBounds(layerBounds);
            if (width < layerBounds.width) {
                width = layerBounds.width;
            }
            if (height < layerBounds.height) {
                height = layerBounds.height;
            }
        }

        colNum = width;
        rowNum = height;
    }

    /**
     * 
     * @return
     */
    public int getTotalTileSets() {
        return tilesets.size();
    }

    /**
     * Returns a vector with the currently loaded tilesets.
     *
     * @return Vector
     */
    public ArrayList<TileSet> getTileSets() {
        return tilesets;
    }
}
