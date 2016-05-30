/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author Administrator
 */
public class UIUtil {

    /**
     * 
     * @param font
     */
    public static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys();
            keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
    private static long time;

    /**
     * 水波渲染,绘制波动的图像。
     * @author Sol
     *
     * @param img 图像源
     * @param inverted 图像是否翻转
     * @param power 波能(0.0-1.0)
     * @param x_src 图像拷贝起始象素X坐标
     * @param y_src 图像拷贝起始象素Y坐标
     * @param width 图像拷贝宽
     * @param height 图像拷贝高
     * @param x_dest 锚点的象素X坐标
     * @param y_dest 锚点的象素Y坐标
     * @return 
     * @throws IllegalArgumentException 如果参数错误则抛出此异常
     */
    public static BufferedImage drawWaveRenderer(BufferedImage img, boolean inverted,
        double power, int x_src, int y_src, int width, int height,
        int x_dest, int y_dest) throws IllegalArgumentException {
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffImg.createGraphics();
        if (power < 0 || power > 1) {
            throw new IllegalArgumentException();
        }

        if (x_src < 0 || y_src < 0 || width < 0 || height < 0
            || x_src + width > img.getWidth()
            || y_src + height > img.getHeight()) {
            throw new IllegalArgumentException();
        }
        int swing = height >> 3;
        int offsetX = 0;
        int offsetY = 0;
        for (int n = 0, offset; n < height; n++) {
            offset = (int) ((swing * (n + 20)
                * Math.sin((double) swing * (height - n) / (n + 1) + time) / height) * power);
            if (n + offset < 0) {
                offset = -n;
            } else if (n + offset >= height) {
                offset = height - n - 1;
            }
            g2d.setClip(x_dest + offsetX, y_dest + offsetY + n, width, 1);
//            g2d.drawImage(img, x_dest + offsetX - x_src, y_dest + offsetY - y_src - height + 1 + (n << 1) + offset, null);
            if (inverted) {
                g2d.drawImage(img, x_dest + offsetX - x_src, y_dest + offsetY
                    - y_src - height + 1 + (n << 1) + offset, null);
            } else {
                g2d.drawImage(img, x_dest + offsetX - x_src, y_dest + offsetY
                    - y_src - offset, null);
            }
        }
        return buffImg;
    }
}
