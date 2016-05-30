/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.listener;

import java.util.EventListener;

/**
 *
 * @author Administrator
 */
public interface LayerChangeListener extends EventListener {

    /**
     * 
     * @param event
     */
    void layerChanged(LayerChangedEvent event);

    /**
     * 
     * @param event
     * @param nowBool
     */
    void visibleChanged(LayerChangedEvent event, boolean nowBool);

    /**
     * 
     * @param event
     * @param oldName
     * @param newName
     */
    void nameChanged(LayerChangedEvent event, String oldName, String newName);
}
