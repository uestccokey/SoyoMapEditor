/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.ui.brush;

import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.SelectionLayer;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 *
 * @author Administrator
 */
public abstract class AbBrush extends Map implements IBrush {

    /**
     *
     */
    protected Map affectedMp;
    /**
     *
     */
    protected int initLayer;
    /**
     *
     */
    protected boolean paintingStarted = false;

    /**
     *
     */
    public AbBrush() {
    }

    /**
     *
     * @param ab
     */
    public AbBrush(AbBrush ab) {
    }

    /**
     *
     * @return
     */
    public boolean isPaintingStarted() {
        return paintingStarted;
    }

    public void startPaint(Map mp, int x, int y, int button, int layer) {
        affectedMp = mp;
        initLayer = layer;
        paintingStarted = true;
    }

    public Rectangle doPaint(int x, int y) throws Exception {

        if (!paintingStarted) {
            throw new Exception("Attempted to call doPaint() without calling startPaint()!");
        }
        return null;
    }

    public void endPaint() {
        paintingStarted = false;
    }

    /**
     *
     * @return
     */
    public abstract Shape getShape();
}
