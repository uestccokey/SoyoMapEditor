/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Administrator
 */
public class Configuration {

    /**
     * 最近文件列表
     */
    public static final int RECENT_FILE_COUNT = 16;
    private static final Preferences prefs = Preferences.userRoot().node("soyomap");
    private static final Preferences rightEraserPrefs = Preferences.userRoot().node("soyomap.eraser");//右键橡皮修改
    private static final Preferences skinPrefs = Preferences.userRoot().node("soyomap.skin");         //默认皮肤修改
    private static final byte IS_RIGHT_ERASER = 0;

    private Configuration() {
    }

    /**
     * 
     * @return
     */
    public static String getSkin() {
        return skinPrefs.get("SKIN", "org.pushingpixels.substance.api.skin.SubstanceMarinerLookAndFeel");
    }

    /**
     * 
     * @param skin
     */
    public static void setSkin(String skin) {
        skinPrefs.put("SKIN", skin);
    }

    /**
     * 
     * @return
     */
    public static boolean getIsRightEraser() {
        return rightEraserPrefs.getBoolean(IS_RIGHT_ERASER + "", false);
    }

    /**
     * 
     * @param bool
     */
    public static void saveIsRightEraser(boolean bool) {
        rightEraserPrefs.putBoolean(IS_RIGHT_ERASER + "", bool);
    }

    /**
     * Returns the node with the given path name relative from the root of
     * Tiled configuration.
     *
     * @param pathName the path name relative from the root
     * @return the requested preferences node
     */
    public static Preferences node(String pathName) {
        return prefs.node(pathName);
    }

    /**
     * Returns the root node for Tiled configuration.
     *
     * @return the root node for Tiled configuration
     */
    public static Preferences root() {
        return prefs;
    }

    /**
     * Returns the list of recently used files.
     *
     * @return the list of recently used files
     */
    public static List<String> getRecentFiles() {
        List<String> recent = new ArrayList<String>(RECENT_FILE_COUNT);
        Preferences recentNode = prefs.node("recent");
        for (int i = 0; i < RECENT_FILE_COUNT; i++) {
            String recentFile = recentNode.get("file" + i, "");
            if (recentFile.length() > 0) {
                if (new File(recentFile).exists() && new File(recentFile).isDirectory()) {
                    recent.add(recentFile);
                }
            }
        }
        return recent;
    }

    /**
     * Adds the given filename to the top of the recent file list. It also
     * makes sure it does not occur further down the list.
     *
     * @param projectFile 
     */
    public static void addToRecentFiles(String projectFile) {
        assert projectFile != null;
        // Store the new recent file listing
        Preferences recentNode = prefs.node("recent");
        // Get the existing recent file list
        List<String> recent = getRecentFiles();
        // Remove all existing occurences of the file
        Iterator iterator = recent.iterator();
        while (iterator.hasNext()) {
            String filename = (String) iterator.next();
            if (filename.equals(projectFile)) {
                iterator.remove();
            }
        }
        try {
            recentNode.clear();
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
        }
        // Add the given map file to the top
        recent.add(0, projectFile);
        for (int i = 0; i < RECENT_FILE_COUNT && i < recent.size(); i++) {
            String recentFile = recent.get(i);
            recentNode.put("file" + i, recentFile);
        }
    }
}
