/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.ui.render;

import com.soyostar.editor.map.model.Map;

/**
 *
 * @author 地图绘制器工厂，未完成
 */
public class MapRenderFactory {

    /**
     * 先检查有没有以插件扩展的地图view，没有则默认用orthomapview
     * @param map 
     * @return
     */
    public static MapRender createMapRender(Map map) {
        return new OrthoMapRender(map);
    }
}
