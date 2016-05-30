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
public interface IMapReader {

    /**
     * 
     * @param mapFile
     * @return
     * @throws Exception
     */
    public Map readMap(String mapFile) throws Exception;
}
