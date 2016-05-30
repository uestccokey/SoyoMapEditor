/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.ui.widget;

import java.awt.Component;
import java.util.EventObject;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * 
 * @author Administrator
 */
public class JTableComboBoxEditor extends JComboBox implements TableCellEditor {

    /**
     *
     */
    protected EventListenerList listenerLs = new EventListenerList();
    /**
     *
     */
    protected ChangeEvent changeEvent = new ChangeEvent(this);

    /**
     *
     * @param items
     */
    public JTableComboBoxEditor(Object[] items) {
        super(items);
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                fireEditingStopped();
            }
        });
    }

    public void addCellEditorListener(CellEditorListener listener) {
        listenerLs.add(CellEditorListener.class, listener);
    }

    public void removeCellEditorListener(CellEditorListener listener) {
        listenerLs.remove(CellEditorListener.class, listener);
    }

    /**
     *
     */
    protected void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = listenerLs.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingStopped(changeEvent);
            }
        }
    }

    /**
     *
     */
    protected void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = listenerLs.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(changeEvent);
            }
        }
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public boolean isCellEditable(EventObject event) {
        return true;
    }

    public boolean shouldSelectCell(EventObject event) {
        return true;
    }

    public Object getCellEditorValue() {
        return getSelectedItem();
    }

    public Component getTableCellEditorComponent(JTable table,
            Object value, boolean isSelected, int row, int column) {
        setSelectedItem(value);
        return this;
    }
}
