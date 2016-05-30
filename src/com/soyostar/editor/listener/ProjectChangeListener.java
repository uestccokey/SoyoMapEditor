/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.listener;

import com.soyostar.editor.map.model.Map;
import java.util.EventListener;

/**
 *
 * @author Administrator
 */
public interface ProjectChangeListener extends EventListener {

    /**
     * 
     * @param e
     */
    public void projectChanged(ProjectChangedEvent e);

    /**
     * 
     * @param e
     * @param map
     */
    public void mapAdded(ProjectChangedEvent e, Map map);

    /**
     * 
     * @param e
     * @param index
     */
    public void mapRemoved(ProjectChangedEvent e, int index);
}
