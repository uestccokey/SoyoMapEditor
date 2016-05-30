/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.listener;

import com.soyostar.editor.map.model.MapTile;
import java.util.EventObject;

/**
 * An event that describes the selection of a tile.
 *
 * @version $Id$
 */
public class TileSelectionEvent extends EventObject
{
    private MapTile tile;

    /**
     *
     * @param source
     * @param tile
     */
    public TileSelectionEvent(Object source, MapTile tile) {
        super(source);
        this.tile = tile;
    }

    /**
     *
     * @return
     */
    public MapTile getTile() {
        return tile;
    }
}