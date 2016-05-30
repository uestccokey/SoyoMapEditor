/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.model;

import com.soyostar.editor.map.model.TileSet;
import com.soyostar.editor.map.model.Layer;
import com.soyostar.editor.map.main.AppData;
import com.soyostar.editor.map.listener.MapChangeListener;
import com.soyostar.editor.map.listener.MapChangedEvent;
import com.soyostar.editor.map.listener.TilesetChangeListener;
import com.soyostar.editor.map.listener.TilesetChangedEvent;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Administrator
 */
public class TileSetTableModel extends AbstractTableModel implements MapChangeListener, TilesetChangeListener {

    private static final String TILESET_COLUMN_NAME[] = {
        "序号", "名称", "源文件", "所属地图"
    };
    private static final Class TILESET_COLUMN_CLASS[] = {
        Integer.class, String.class, String.class, String.class
    };

    @Override
    public String getColumnName(int c) {
        return TILESET_COLUMN_NAME[c];
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return TILESET_COLUMN_CLASS[c];
    }

    public int getColumnCount() {
        return TILESET_COLUMN_NAME.length;
    }
    private AppData data = AppData.getInstance();

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return ((TileSet)data.getCurrentMap().getTileSets().get(rowIndex)).getIndex();
            case 1:
                return ((TileSet)data.getCurrentMap().getTileSets().get(rowIndex)).getName();
            case 2:
                return ((TileSet)data.getCurrentMap().getTileSets().get(rowIndex)).getTilebmpFile();
            case 3:
                return ((TileSet)data.getCurrentMap().getTileSets().get(rowIndex)).getMap().getName();
        }
        return null;
    }

    @Override
    public void setValueAt(Object v, int r, int c) {
        switch (c) {
            case 1:
                ((TileSet)data.getCurrentMap().getTileSets().get(r)).setName((String) v);
                break;
        }
        this.fireTableCellUpdated(r, c);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        switch (col) {
            case 1:
                return true;
        }
        return false;
    }

    public int getRowCount() {
        if (data.getCurrentMap() == null) {
            return 0;
        }
        return data.getCurrentMap().getTileSets().size();
    }

    /**
     * 
     * @param e
     */
    public void mapChanged(MapChangedEvent e) {
    }

    /**
     * 
     * @param e
     * @param layer
     */
    public void layerAdded(MapChangedEvent e, Layer layer) {
    }

    /**
     * 
     * @param e
     * @param index
     */
    public void layerRemoved(MapChangedEvent e, int index) {
    }

    /**
     * 
     * @param e
     * @param tileset
     */
    public void tilesetAdded(MapChangedEvent e, TileSet tileset) {
        int index = data.getCurrentMap().getTileSets().indexOf(tileset);
        if (index == -1) {
            return;
        }
//        System.out.println("tileset add listener!");
        tileset.addTilesetChangeListener(this);
        fireTableRowsInserted(index, index);
    }

    /**
     * 
     * @param e
     * @param index
     */
    public void tilesetRemoved(MapChangedEvent e, int index) {
        fireTableRowsDeleted(index - 1, index);
    }

    /**
     * 
     * @param e
     * @param index0
     * @param index1
     */
    public void tilesetsSwapped(MapChangedEvent e, int index0, int index1) {
        fireTableRowsUpdated(index0, index1);
    }

    /**
     * 
     * @param event
     */
    public void tilesetChanged(TilesetChangedEvent event) {
    }

    /**
     * 
     * @param event
     * @param oldName
     * @param newName
     */
    public void nameChanged(TilesetChangedEvent event, String oldName, String newName) {
        int index = data.getCurrentMap().getTileSets().indexOf(event.getTileset());

        if (index == -1) {
            return;
        }

        fireTableCellUpdated(index, 0);
    }

    /**
     * 
     * @param event
     * @param oldSource
     * @param newSource
     */
    public void sourceChanged(TilesetChangedEvent event, String oldSource, String newSource) {
        int index = data.getCurrentMap().getTileSets().indexOf(event.getTileset());

        if (index == -1) {
            return;
        }

        fireTableCellUpdated(index, 1);
    }

    /**
     * 
     * @param e
     * @param newBool
     */
    public void layerVisibled(MapChangedEvent e, boolean newBool) {
    }
}
