/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Cuts tiles from a tileset image according to a regular rectangular pattern.
 * Supports a variable spacing between tiles and a margin around them.
 */
public class TileCutter {

    private int nextX, nextY;
    private BufferedImage image;
    private final int tileWidth;
    private final int tileHeight;

    /**
     *
     * @param tileWidth
     * @param tileHeight
     */
    public TileCutter(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    /**
     *
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     *
     * @return
     */
    public Image getNextTile() {
        if (nextY + tileHeight <= image.getHeight()) {
            BufferedImage tile =
                image.getSubimage(nextX, nextY, tileWidth, tileHeight);
            nextX += tileWidth;

            if (nextX + tileWidth > image.getWidth()) {
                nextX = 0;
                nextY += tileHeight;
            }
            return tile;
        }
        return null;
    }

    /**
     *
     * @return
     */
    public Dimension getTileDimensions() {
        return new Dimension(tileWidth, tileHeight);
    }

    /**
     * Returns the number of tiles per row in the tileset image.
     * @return the number of tiles per row in the tileset image.
     */
    public int getTilesPerRow() {
        return image.getWidth() / tileWidth;
    }
}
