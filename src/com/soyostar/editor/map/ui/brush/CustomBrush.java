/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.ui.brush;

import com.soyostar.editor.map.model.Layer;
import com.soyostar.editor.map.model.Map;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ListIterator;

/**
 *
 * @author Administrator
 */
public class CustomBrush extends AbBrush {

    /**
     *
     * @param mlp
     */
    public CustomBrush(Map mlp) {
        addAllLayers(mlp.getLayerArrayList());
        fitBoundsToLayers();
    }

    /**
     *
     * @param mapLayer
     */
    public CustomBrush(Layer mapLayer) {
        addLayer(mapLayer);
        fitBoundsToLayers();
    }

    /**
     *
     * @return
     */
    public Shape getShape() {
        return getBounds();
    }

    /**
     * Determines whether this brush is equal to another brush.
     * @param b
     */
    public boolean equals(IBrush b) {
        if (b instanceof CustomBrush) {
            if (b == this) {
                return true;
            } else {
                //TODO: THIS
            }
        }
        return false;
    }

    @Override
    public void startPaint(Map mp, int x, int y, int button, int layer) {
        super.startPaint(mp, x, y, button, layer);
    }

    /**
     * The custom brush will merge its internal layers onto the layers of the
     * specified MultilayerPlane.
     *
     * @see tiled.core.TileLayer#mergeOnto(tiled.core.MapLayer)
     * @see tiled.mapeditor.brush.Brush#doPaint(int, int)
     * @throws Exception
     */
    @Override
    public Rectangle doPaint(int x, int y) throws Exception {

        int layer = initLayer;
        int centerx = x - colNum / 2;
        int centery = y - rowNum / 2;

        super.doPaint(x, y);

        ListIterator itr = getListLayers();
        while (itr.hasNext()) {
            Layer tl = (Layer) itr.next();
            Layer tm = (Layer) affectedMp.getLayer(layer++);
            if (tm != null && tm.isIsVisible()) {
                tl.setOffset(centerx, centery);
                tl.mergeOnto(tm);
            }
        }
        return new Rectangle(centerx, centery, colNum, rowNum);
    }
}
