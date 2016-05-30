/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.map.model;

/**
 *
 * @author Administrator
 */
public class Area {

    int area = -2;

    /**
     *
     */
    public Area() {
    }
    /**
     *
     * @param x
     * @param y
     */
    public Area(int x, int y) {
        col = x;
        row = y;
    }
    /**
     *
     * @param x
     * @param y
     * @param area
     */
    public Area(int x, int y, int area) {
        col = x;
        row = y;
        this.area = area;
    }

    /**
     *
     * @return
     */
    public int getCol() {
        return col;
    }

    /**
     *
     * @param col
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     *
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }
    int row;
    int col;

    /**
     *
     * @return
     */
    public int getArea() {
        return area;
    }

    /**
     *
     * @param area
     */
    public void setArea(int area) {
        this.area = area;
    }
}
