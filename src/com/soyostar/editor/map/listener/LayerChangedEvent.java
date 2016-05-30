/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.listener;

import com.soyostar.editor.map.model.Layer;
import java.util.EventObject;

/**
 *
 * @author Administrator
 */
public class LayerChangedEvent extends EventObject {

    /**
     * 
     * @param layer
     */
    public LayerChangedEvent(Layer layer) {
        super(layer);
    }

    /**
     * 
     * @return
     */
    public Layer getLayer() {
        return (Layer) getSource();
    }
}
