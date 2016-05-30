/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.io;

import com.soyostar.editor.info.SoftInformation;
import com.soyostar.editor.map.model.Layer;
import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.MapObject;
import com.soyostar.editor.map.model.ObjectLayer;
import com.soyostar.editor.map.model.MapTile;
import com.soyostar.editor.map.model.TileLayer;
import com.soyostar.editor.map.model.TileSet;
import com.soyostar.editor.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author Administrator
 */
public class DefaultMapXMLWriter implements IMapWriter {

    private static final int LAST_BYTE = 0x000000FF;

    public void writeProperties(HashMap<String, String> properties, Element element) {
        if (!properties.isEmpty()) {
            Iterator iter1 = properties.entrySet().iterator();
            Element propertysElement = element.addElement("properties");
            while (iter1.hasNext()) {
                Element propertyElement = propertysElement.addElement("property");
                java.util.Map.Entry entry = (java.util.Map.Entry) iter1.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                propertyElement.addAttribute("name", key.toString());
                propertyElement.addAttribute("value", val.toString());
            }
        }
    }

    public void writeMap(Map map, String filename) throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element mapElement = doc.addElement("map");
        mapElement.addAttribute("version", SoftInformation.majorVersion + "." + SoftInformation.minorVersion);

        mapElement.addAttribute("name", "" + map.getName());
        mapElement.addAttribute("gid", "" + map.getIndex());
        mapElement.addAttribute("orientation", map.getMapOrientation());
        mapElement.addAttribute("width", "" + map.getWidth());
        mapElement.addAttribute("height", "" + map.getHeight());
        mapElement.addAttribute("tilewidth", "" + map.getTileWidth());
        mapElement.addAttribute("tileheight", "" + map.getTileHeight());
        writeProperties(map.getProperties(), mapElement);
        int tn = map.getTotalTileSets();
        int tgid = 1;
        for (int i = 0; i < tn; i++) {
            TileSet tileset = map.getTileSet(i);
            tileset.setFirstGid(tgid);
            tgid += tileset.getMaxTileId() + 1;
            Element tilesetElement = mapElement.addElement("tileset");
            tilesetElement.addAttribute("name", tileset.getName());
            tilesetElement.addAttribute("firstgid", "" + tileset.getFirstGid());
            tilesetElement.addAttribute("tilewidth", "" + tileset.getTileWidth());
            tilesetElement.addAttribute("tileheight", "" + tileset.getTileHeight());
            Element tilesetImageElement = tilesetElement.addElement("image");
            tilesetImageElement.addAttribute("source", File.separator + "image"
                    + File.separator + "tileset" + File.separator + tileset.getTilebmpFile());
            Iterator tileIterator = tileset.iterator();
            while (tileIterator.hasNext()) {
                MapTile tile = (MapTile) tileIterator.next();
                if (tile != null && !tile.getProperties().isEmpty()) {
                    Element tileElement = tilesetElement.addElement("tile");
                    tileElement.addAttribute("id", "" + tile.getIndex());
                    writeProperties(tile.getProperties(), tileElement);
                }
            }
        }

        int ln = map.getTotalLayers();
        for (int i = 0; i < ln; i++) {
            Layer layer = map.getLayer(i);
            if (layer instanceof TileLayer) {
                Element layerElement = mapElement.addElement("layer");
                writeProperties(layer.getProperties(), layerElement);
                layerElement.addAttribute("name", layer.getName());
                layerElement.addAttribute("width", "" + layer.getWidth());
                layerElement.addAttribute("height", "" + layer.getHeight());
                layerElement.addAttribute("x", "" + layer.getBounds().x);
                layerElement.addAttribute("y", "" + layer.getBounds().y);
                if (!layer.isIsVisible()) {
                    layerElement.addAttribute("visible", "" + (layer.isIsVisible() ? 1 : 0));
                }
                Element dataElement = layerElement.addElement("data");
                dataElement.addAttribute("encoding", "base64");
                dataElement.addAttribute("compression", "gzip");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                OutputStream out = new GZIPOutputStream(baos);
                for (int y = 0; y < layer.getHeight(); y++) {
                    for (int x = 0; x < layer.getWidth(); x++) {
                        MapTile tile = ((TileLayer) layer).getTileAt(x + layer.getBounds().x,
                                y + layer.getBounds().y);
                        int gid = 0;
                        if (tile != null) {
                            gid = tile.getIndex() + tile.getTileSet().getFirstGid();
                        }
                        out.write(gid & LAST_BYTE);
                        out.write(gid >> 8 & LAST_BYTE);
                        out.write(gid >> 16 & LAST_BYTE);
                        out.write(gid >> 24 & LAST_BYTE);
                    }
                }
                ((GZIPOutputStream) out).finish();
                dataElement.addText(new String(Base64.encode(baos.toByteArray())));
            } else if (layer instanceof ObjectLayer) {
                Element layerElement = mapElement.addElement("objectgroup");
                writeProperties(layer.getProperties(), layerElement);
                layerElement.addAttribute("name", layer.getName());
                layerElement.addAttribute("width", "" + layer.getWidth());
                layerElement.addAttribute("height", "" + layer.getHeight());
                layerElement.addAttribute("x", "" + layer.getBounds().x);
                layerElement.addAttribute("y", "" + layer.getBounds().y);
                if (!layer.isIsVisible()) {
                    layerElement.addAttribute("visible", "" + (layer.isIsVisible() ? 1 : 0));
                }
                Iterator<MapObject> itr = ((ObjectLayer) layer).getObjects();
                while (itr.hasNext()) {
                    MapObject object = (MapObject) itr.next();
//                    if (object.getWidth() == 0 || object.getHeight() == 0) {
//                        //忽略对象层中高宽为0,即不可视或无用的
//                        continue;
//                    }
                    Element objectElement = layerElement.addElement("object");
                    objectElement.addAttribute("name", object.getName());
                    objectElement.addAttribute("x", object.getX() + "");
                    objectElement.addAttribute("y", object.getY() + "");
                    objectElement.addAttribute("width", object.getWidth() + "");
                    objectElement.addAttribute("height", object.getHeight() + "");
                }
            }
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter xmlw = new XMLWriter(new FileWriter(filename), format);
        xmlw.write(doc);
        xmlw.close();
    }
}
