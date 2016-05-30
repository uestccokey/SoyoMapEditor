/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.uodoredo;

import com.soyostar.editor.map.main.AppData;
import com.soyostar.editor.map.ui.render.MapRender;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;
import javax.swing.event.UndoableEditEvent;

/**
 *
 * @author Administrator
 */
public class UndoRedoHandler extends UndoManager {

    private UndoableEdit savedAt;
    private MapRender render;

    /**
     * 
     * @param aThis
     */
    public UndoRedoHandler(MapRender aThis) {
        render = aThis;
        setLimit(256);//设置返回层数限制是256次
    }

    /**
     * Overridden to update the undo/redo actions.
     * @see UndoManager#undo()
     * @throws CannotUndoException
     */
    @Override
    public synchronized void undo() throws CannotUndoException {
        if (super.canUndo()) {
            super.undo();
            render.repaint();
        }

        // todo: Updating of the mapview should ultimately happen
        // todo: automatically based on the changes made to the map.

    }

    /**
     * Overridden to update the undo/redo actions.
     * @see UndoManager#redo()
     * @throws CannotRedoException
     */
    @Override
    public synchronized void redo() throws CannotRedoException {
        if (super.canRedo()) {
            super.redo();
            render.repaint();
        }

    }

    /**
     * 
     * @return
     */
    public boolean isAllSaved() {
        return editToBeUndone() == savedAt;
    }

    /**
     * 
     */
    public void commitSave() {
        savedAt = editToBeUndone();
    }

//    @Override
//    public void undoableEditHappened(UndoableEditEvent e) {
//        super.undoableEditHappened(e);
//        for (int i = 0, n = getEdits().length; i < n; i++) {
//            System.out.println(i + " " + getEdits()[i]);
//        }
//    }
    /**
     * 
     * @return
     */
    public String[] getEdits() {
        String[] list = new String[edits.size()];
        Iterator<UndoableEdit> itr = edits.iterator();
        int i = 0;

        while (itr.hasNext()) {
            UndoableEdit e = itr.next();
            list[i++] = e.getPresentationName();
        }

        return list;
    }
}
