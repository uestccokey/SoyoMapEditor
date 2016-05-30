/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.listener;

import java.util.EventListener;

/**
 * @version $Id$
 */
public interface TileSelectionListener extends EventListener
{
    /**
     *
     * @param e
     */
    public void tileSelected(TileSelectionEvent e);

    /**
     *
     * @param e
     */
    public void tileRegionSelected(TileRegionSelectionEvent e);
}