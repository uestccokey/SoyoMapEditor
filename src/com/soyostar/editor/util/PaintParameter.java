/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.util;

import java.awt.Rectangle;

/**
 * 绘制参数类
 * @author Administrator
 */
public class PaintParameter {

    /**
     *
     * @return
     */
    public boolean isMirror() {
        return mirror;
    }

    /**
     *
     * @param mirror
     */
    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    /**
     *
     * @return
     */
    public Rectangle getRec() {
        return rec;
    }

    /**
     *
     * @param rec
     */
    public void setRec(Rectangle rec) {
        this.rec = rec;
    }

    /**
     *
     * @return
     */
    public int getRotate() {
        return rotate;
    }

    /**
     *
     * @param rotate
     */
    public void setRotate(int rotate) {
        this.rotate = rotate;
    }
    private float zoom;//缩放

    /**
     *
     * @return
     */
    public float getZoom() {
        return zoom;
    }

    /**
     *
     * @param zoom
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
    private Rectangle rec = new Rectangle();//位置，大小
    private boolean mirror;//镜像
    private int rotate;//翻转角度
}
