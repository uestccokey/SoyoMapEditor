/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.uodoredo;

import com.soyostar.editor.map.model.Layer;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 *
 * @author Administrator
 */
public class LayerEdit extends AbstractUndoableEdit {

    private final Layer editedLayer;
    private Layer layerUndo, layerRedo;
    private String name;
    private boolean inProgress;

    /**
     * 
     * @param layer
     */
    public LayerEdit(Layer layer) {
        editedLayer = layer;
    }

    /**
     * 
     * @param layer
     * @param before
     */
    public LayerEdit(Layer layer, Layer before) {
        this(layer);
        start(before);
    }

    /**
     * 
     * @param layer
     * @param before
     * @param after
     */
    public LayerEdit(Layer layer, Layer before, Layer after) {
        this(layer, before);
        end(after);
    }

    private void start(Layer fml) {
        layerUndo = fml;
        inProgress = true;
    }

    /**
     * 
     * @param fml
     */
    public void end(Layer fml) {
        if (!inProgress) {
            new Exception("end called before start");
        }
        if (fml != null) {
            layerRedo = fml;
            inProgress = false;
        }
    }

    /**
     * 
     * @return
     */
    public Layer getStart() {
        return layerUndo;
    }

    /* inherited methods */
    @Override
    public void undo() throws CannotUndoException {
        if (editedLayer == null) {
            throw new CannotUndoException();
        }
        layerUndo.copyTo(editedLayer);
    }

    @Override
    public boolean canUndo() {
        return layerUndo != null && editedLayer != null;
    }

    @Override
    public void redo() throws CannotRedoException {
        if (editedLayer == null) {
            throw new CannotRedoException();
        }
        layerRedo.copyTo(editedLayer);
    }

    @Override
    public boolean canRedo() {
        return layerRedo != null && editedLayer != null;
    }

    @Override
    public void die() {
        layerUndo = null;
        layerRedo = null;
        inProgress = false;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        if (inProgress && anEdit.getClass() == getClass()) {
            //TODO: absorb the edit
            //return true;
        }
        return false;
    }

    /**
     * 
     * @param s
     */
    public void setPresentationName(String s) {
        name = s;
    }

    @Override
    public String getPresentationName() {
        return name;
    }
}
