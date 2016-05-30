/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.project;

import com.soyostar.editor.listener.ProjectChangeListener;
import com.soyostar.editor.listener.ProjectChangedEvent;
import com.soyostar.editor.map.model.Map;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Administrator
 */
public class Project {

    private String name = "";//项目标题名
    private String path = "";//路径名
    private String softVersion = "";//项目对应的软件版本
    private final List projectChangeListeners = new LinkedList();
    //FIXME 应该加入一个MapManager类来管理map
    private HashMap<Integer, Map> maps = new HashMap<Integer, Map>();

    /**
     * 
     * @return
     */
    public HashMap<Integer, Map> getMaps() {
        return maps;
    }

    /**
     * 
     * @param map
     */
    public void addMap(Map map) {
        int id = getMapMaxIndex() + 1;
        maps.put(id, map);
        map.setIndex(id);
        fireMapAdded(map);
    }

    /**
     * 
     * @param map
     * @param id
     */
    public void addMap(Map map, int id) {
        maps.put(id, map);
        fireMapAdded(map);
    }

    /**
     * 
     */
    public void removeAllMap() {
        maps.clear();
    }

    /**
     * 
     * @param index
     */
    public void removeMap(int index) {
        maps.remove(index);
        fireMapRemoved(index);
    }

    /**
     * 
     * @param id
     * @return
     */
    public Map getMap(int id) {
        return maps.get(id);
    }

    /**
     * 
     * @return
     */
    public int getMapMaxIndex() {
        int max = -1;
        Set<Integer> mapset = maps.keySet();
        Iterator it = mapset.iterator();
        while (it.hasNext()) {
            Integer in = (Integer) it.next();
            if (in > max) {
                max = in;
            }
        }
        return max;
    }

    /**
     *
     * @return
     */
    public int getMapCounts() {
        return maps.size();
    }

    /**
     * 
     * @param listener
     */
    public void addProjectChangeListener(ProjectChangeListener listener) {
        projectChangeListeners.add(listener);
    }

    /**
     * Removes a change listener.
     * @param listener the listener to remove
     */
    public void removeProjectChangeListener(ProjectChangeListener listener) {
        projectChangeListeners.remove(listener);
    }

    /**
     * Notifies all registered map change listeners about a change.
     */
    protected void fireProjectChanged() {
        Iterator iterator = projectChangeListeners.iterator();
        ProjectChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new ProjectChangedEvent(this);
            }
            ((ProjectChangeListener) iterator.next()).projectChanged(event);
        }
    }

    /**
     * Notifies all registered map change listeners about a change.
     * @param map 
     */
    protected void fireMapAdded(Map map) {
        Iterator iterator = projectChangeListeners.iterator();
        ProjectChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new ProjectChangedEvent(this);
            }
            ((ProjectChangeListener) iterator.next()).mapAdded(event, map);
        }
    }

    /**
     * Notifies all registered map change listeners about a change.
     * @param index 
     */
    protected void fireMapRemoved(int index) {
        Iterator iterator = projectChangeListeners.iterator();
        ProjectChangedEvent event = null;

        while (iterator.hasNext()) {
            if (event == null) {
                event = new ProjectChangedEvent(this);
            }
            ((ProjectChangeListener) iterator.next()).mapRemoved(event, index);
        }
    }

    /**
     *
     * @return
     */
    public String getSoftVersion() {
        return softVersion;
    }

    /**
     *
     * @param softVersion
     */
    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
        fireProjectChanged();
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        fireProjectChanged();
    }

    /**
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
        fireProjectChanged();
    }

    /**
     *
     */
    public static final class Filter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            if (f.getName().equals("Project.xml")) {
                return true;
            }
            return false;
        }

        public String getDescription() {
            return "工程文件 (Project.xml)";
        }
    }
}
