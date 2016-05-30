/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.io;

import com.soyostar.editor.map.main.AppData;
import com.soyostar.editor.map.model.Map;
import com.soyostar.editor.map.model.MapObject;
import com.soyostar.editor.map.model.ObjectLayer;
import com.soyostar.editor.map.model.MapTile;
import com.soyostar.editor.map.model.TileLayer;
import com.soyostar.editor.map.model.TileSet;
import com.soyostar.editor.util.Base64;
import com.soyostar.editor.util.TileCutter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author Administrator
 */
public class DefaultMapXMLReader implements IMapReader {

    private AppData data = AppData.getInstance();

    public void readProperties(Element node, HashMap<String, String> properties) {
        if (node.getName().equalsIgnoreCase("property")) {
            properties.put(node.attributeValue("name"), node.attributeValue("value"));
        } else if (node.getName().equalsIgnoreCase("properties")) {
            for (Iterator i = node.elementIterator("property"); i.hasNext();) {
                Element element = (Element) i.next();
                readProperties(element, properties);
            }
        }
    }

    public Map readMap(String mapFile) throws Exception {
        Map map = new Map();
        SAXReader sax = new SAXReader();
        try {
            Document document = sax.read(new File(mapFile));
            // 得到根元素
            Element root = document.getRootElement();
            if (root.attributeValue("name") != null) {
                map.setName(root.attributeValue("name"));
            }
            if (root.attributeValue("gid") != null) {
                map.setIndex(Integer.parseInt(root.attributeValue("gid")));
            }
            if (root.element("properties") != null) {
                readProperties(root.element("properties"), map.getProperties());
            }
            map.setMapOrientation(root.attributeValue("orientation"));
            map.setWidth(Integer.parseInt(root.attributeValue("width")));
            map.setHeight(Integer.parseInt(root.attributeValue("height")));
            map.setTileWidth(Integer.parseInt(root.attributeValue("tilewidth")));
            map.setTileHeight(Integer.parseInt(root.attributeValue("tileheight")));
            // 枚举节点
            for (Iterator i = root.elementIterator("tileset"); i.hasNext();) {
                Element tilesetElement = (Element) i.next();
                TileSet tileset = new TileSet();
                tileset.setFirstGid(Integer.parseInt(tilesetElement.attributeValue("firstgid")));
                tileset.setName(tilesetElement.attributeValue("name"));
                tileset.setMap(map);
                Element tilesetsImg = tilesetElement.element("image");
                String path = tilesetsImg.attributeValue("source");
                tileset.importTileBitmap(AppData.getInstance().getCurProject().getPath() + path,
                        new TileCutter(map.getTileWidth(), map.getTileHeight()));
                for (Iterator ii = tilesetElement.elementIterator("tile"); ii.hasNext();) {
                    Element tileElement = (Element) ii.next();
                    MapTile tile = tileset.getTile(Integer.parseInt(tileElement.attributeValue("id")));
                    readProperties(tileElement.element("properties"), tile.getProperties());
                }
                map.addTileset(tileset);
            }
            for (Iterator i = root.elementIterator("layer"); i.hasNext();) {
                Element layerElement = (Element) i.next();
                TileLayer layer = new TileLayer();
                layer.addLayerChangeListener(data.getMainFrame());
                layer.setName(layerElement.attributeValue("name"));
                if (layerElement.attributeValue("visible") != null) {
                    layer.setIsVisible(Boolean.parseBoolean(layerElement.attributeValue("visible")));
                }
                int lx = 0, ly = 0;
                if (layerElement.attributeValue("x") != null) {
                    lx = Integer.parseInt(layerElement.attributeValue("x"));
                }
                if (layerElement.attributeValue("y") != null) {
                    ly = Integer.parseInt(layerElement.attributeValue("y"));
                }
                layer.resize(Integer.parseInt(layerElement.attributeValue("width")),
                        Integer.parseInt(layerElement.attributeValue("height")),
                        lx,
                        ly);
                if (layerElement.element("properties") != null) {
                    readProperties(layerElement.element("properties"), layer.getProperties());
                }
                Element dataElement = layerElement.element("data");
                String encoding = dataElement.attributeValue("encoding");
                String compression = dataElement.attributeValue("compression");
                if (encoding != null && "base64".equalsIgnoreCase(encoding)) {
                    char[] enc = dataElement.getText().trim().toCharArray();
                    byte[] dec = Base64.decode(enc);
                    ByteArrayInputStream bais = new ByteArrayInputStream(dec);
                    InputStream is;
                    if (compression != null && "gzip".equalsIgnoreCase(compression)) {
                        is = new GZIPInputStream(bais);

                        for (int y = 0; y < layer.getHeight(); y++) {
                            for (int x = 0; x < layer.getWidth(); x++) {
                                int tileId = 0;
                                tileId |= is.read();
                                tileId |= is.read() << 8;
                                tileId |= is.read() << 16;
                                tileId |= is.read() << 24;

                                TileSet ts = map.findTileSetForTileGID(tileId);
                                if (ts != null) {
                                    layer.setTileAt(x, y,
                                            ts.getTile(tileId - ts.getFirstGid()));
                                } else {
                                    layer.setTileAt(x, y, null);
                                }
                            }
                        }
                    }
                }
                map.addLayer(layer);
            }
            for (Iterator i = root.elementIterator("objectgroup"); i.hasNext();) {
                Element layerElement = (Element) i.next();
                ObjectLayer layer = new ObjectLayer();
                layer.addLayerChangeListener(data.getMainFrame());
                layer.setName(layerElement.attributeValue("name"));
                if (layerElement.attributeValue("visible") != null) {
                    layer.setIsVisible(Boolean.parseBoolean(layerElement.attributeValue("visible")));
                }
                int lx = 0, ly = 0;
                if (layerElement.attributeValue("x") != null) {
                    lx = Integer.parseInt(layerElement.attributeValue("x"));
                }
                if (layerElement.attributeValue("y") != null) {
                    ly = Integer.parseInt(layerElement.attributeValue("y"));
                }
                layer.resize(Integer.parseInt(layerElement.attributeValue("width")),
                        Integer.parseInt(layerElement.attributeValue("height")),
                        lx,
                        ly);
                if (layerElement.element("properties") != null) {
                    readProperties(layerElement.element("properties"), layer.getProperties());
                }
                for (Iterator ii = layerElement.elementIterator("object"); ii.hasNext();) {
                    Element ele = (Element) ii.next();
                    int elx = 0, ely = 0;
                    if (ele.attributeValue("x") != null) {
                        elx = Integer.parseInt(ele.attributeValue("x"));
                    }
                    if (ele.attributeValue("y") != null) {
                        ely = Integer.parseInt(ele.attributeValue("y"));
                    }
                    MapObject object = new MapObject(elx,
                            ely,
                            Integer.parseInt(ele.attributeValue("width")),
                            Integer.parseInt(ele.attributeValue("height")));
                    layer.addObject(object);
                }
                map.addLayer(layer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return map;
    }
}
