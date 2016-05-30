/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.io;

import com.soyostar.editor.map.model.Map;

/**
 *
 * @author Administrator
 */
public interface IMapWriter {

    /**
     * Saves a map to a file.
     *
     * @param map
     * @param filename the filename of the map file
     * @throws Exception
     */
    public void writeMap(Map map, String filename) throws Exception;
}
