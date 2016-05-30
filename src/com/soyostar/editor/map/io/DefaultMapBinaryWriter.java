/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.io;

import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.ObjectLayer;
import com.soyostar.editor.map.model.TileLayer;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

/**
 *
 * @author Administrator
 */
public class DefaultMapBinaryWriter implements IMapWriter {

    public void writeMap(Map map, String filename) throws Exception {
        FileOutputStream fos = null;
        DataOutputStream dos = null;

        fos = new FileOutputStream(filename);
        dos = new DataOutputStream(fos);
        dos.writeInt(map.getIndex());
        System.out.print("地图序号：" + map.getIndex());
        dos.writeUTF(map.getName());
        System.out.println("地图名称：" + map.getName());
//        System.out.println("音乐名称：" + map.getMusicName());
        dos.writeInt(map.getHeight());
//        System.out.println("地图高度：" + map.getHeight());
        dos.writeInt(map.getWidth());
//        System.out.println("地图宽度：" + map.getWidth());
        dos.writeInt(map.getTileWidth());
//        System.out.println("瓷砖宽度：" + map.getTileWidth());
        dos.writeInt(map.getTileHeight());
//        System.out.println("瓷砖高度：" + map.getTileHeight());
        int tilesetNums = map.getTileSets().size();
        System.out.println("图集总数：" + tilesetNums);
        dos.writeInt(tilesetNums);
        for (int i = 0; i < tilesetNums; i++) {
            dos.writeInt(map.getTileSets().get(i).getIndex());
//            System.out.println("图集ID：" + map.getTileSets().get(i).getIndex());
            dos.writeUTF("\\image\\tileset\\" + map.getTileSets().get(i).getTilebmpFile());
//            System.out.println("图集文件：" + map.getTileSets().get(i).getTilebmpFile());
        }
        int layerNums = map.getLayerArrayList().size();
        System.out.println("层总数：" + layerNums);
        dos.writeInt(layerNums);
        int sLayer = 0;
        for (int i = 0; i < layerNums; i++) {
            if (map.getLayerArrayList().get(i) instanceof ObjectLayer) {
                sLayer = i;
            }
        }
        for (int i = 0; i < layerNums; i++) {
            if (map.getLayerArrayList().get(i) instanceof TileLayer) {
                if (i < sLayer) {
                    dos.writeInt(-(Integer.MAX_VALUE - i));
                } else {
                    dos.writeInt(i);
                }

                for (int j = 0; j < map.getHeight(); j++) {
                    for (int k = 0; k < map.getWidth(); k++) {
                        if (((TileLayer) map.getLayerArrayList().get(i)).getTileAt(k, j) == null) {
                            dos.writeInt(-1);
                            continue;
                        }
                        dos.writeInt(((TileLayer) map.getLayerArrayList().get(i)).getTileAt(k, j).getTileSet().getIndex());
                        dos.writeInt(((TileLayer) map.getLayerArrayList().get(i)).getTileAt(k, j).getIndex());
                    }
                }
            }
        }
//        int areaId = 0;
//        for (int i = 0; i < layerNums; i++) {
//            if (map.getLayerArrayList().get(i) instanceof CollideLayer) {
////                System.out.println("CollideLayer");
//                Area[][] area = new Area[map.getHeight()][map.getWidth()];
//                for (int j = 0; j < map.getHeight(); j++) {
//                    for (int k = 0; k < map.getWidth(); k++) {
//                        area[j][k] = new Area();
//                        area[j][k].setCol(k);
//                        area[j][k].setRow(j);
//                        if (!((CollideLayer) map.getLayerArrayList().get(i)).getCollideAt(k, j)) {
//                            area[j][k].setArea(-2);
//                        } else {
//                            area[j][k].setArea(-1);
//                        }
//                    }
//                }
//                for (int j = 0; j < map.getHeight(); j++) {
//                    for (int k = 0; k < map.getWidth(); k++) {
//
//                        if (area[j][k].getArea() == -2) {
//                            Stack<Area> areaStack = new Stack<Area>();
//                            areaStack.add(area[j][k]);
//                            while (!areaStack.isEmpty()) {
//                                Area are = areaStack.pop();
//                                int ax = are.getCol();
//                                int ay = are.getRow();
//                                if (ax >= 0 && ay >= 0 && ax < map.getWidth() && ay < map.getHeight()
//                                        && are.getArea() == area[ay][ax].getArea()) {
//                                    area[ay][ax].setArea(areaId);
//                                    areaStack.push(new Area(ax, ay - 1));
//                                    areaStack.push(new Area(ax, ay + 1));
//                                    areaStack.push(new Area(ax - 1, ay));
//                                    areaStack.push(new Area(ax + 1, ay));
//                                }
//                            }
//                            areaId++;
//                        }
//                    }
//                }
//                System.out.println("区域数据:");
//                for (int j = 0; j < map.getHeight(); j++) {
//                    for (int k = 0; k < map.getWidth(); k++) {
//                        dos.writeInt(area[j][k].getArea());
//                        if (area[j][k].getArea() >= 0) {
//                            System.out.print("  " + area[j][k].getArea());
//                        } else {
//                            System.out.print(" " + area[j][k].getArea());
//                        }
//                    }
//                    System.out.println();
//                }
//            }
//        }
        for (int i = 0; i < layerNums; i++) {
            if (map.getLayerArrayList().get(i) instanceof ObjectLayer) {
            }
        }
        dos.close();
        fos.close();
    }
}
