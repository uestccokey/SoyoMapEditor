/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.model;

import com.soyostar.editor.map.model.Layer;
import com.soyostar.editor.map.main.AppData;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Administrator
 */
public class LayerTableModel extends AbstractTableModel {

    private static final String LAYER_COLUMN_NAME[] = {
        "可见", "图层名"
    };
    private static final Class LAYER_COLUMN_CLASS[] = {
        Boolean.class, String.class
    };

    @Override
    public String getColumnName(int c) {
        return LAYER_COLUMN_NAME[c];
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return LAYER_COLUMN_CLASS[c];
    }

    public int getColumnCount() {
        return LAYER_COLUMN_NAME.length;
    }
    private AppData data = AppData.getInstance();

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return ((Layer) data.getCurrentMap().getLayerArrayList().get(rowIndex)).isIsVisible();
            case 1:
                return ((Layer) data.getCurrentMap().getLayerArrayList().get(rowIndex)).getName();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object v, int r, int c) {

        switch (c) {
            case 0:
                ((Layer) data.getCurrentMap().getLayerArrayList().get(r)).setIsVisible(!((Layer) data.getCurrentMap().getLayerArrayList().get(r)).isIsVisible());
                break;
            case 1:
                ((Layer) data.getCurrentMap().getLayerArrayList().get(r)).setName((String) v);
                break;
        }
        this.fireTableCellUpdated(r, c);
    }

    public int getRowCount() {

        if (data.getCurrentMap() == null) {
            return 0;
        }
        return data.getCurrentMap().getLayerArrayList().size();
    }
}
