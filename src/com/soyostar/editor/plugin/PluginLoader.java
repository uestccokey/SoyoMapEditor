/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.plugin;

import com.soyostar.editor.map.main.MapEditorFrame;
import com.soyostar.editor.util.FileUtil;
import com.soyostar.editor.util.IOFileFilterCreater;
import com.soyostar.editor.util.Log;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author Administrator
 */
public class PluginLoader extends URLClassLoader {

    private static PluginLoader instance;

    /**
     * 
     * @return
     */
    public int getPluginNum() {
        return plugins.size();
    }

    /**
     *
     */
    private PluginLoader() {
        super(new URL[0]);
    }

    private JButton getButton(int id) {
        Plugin p = plugins.get(id);
        JButton item = new JButton(p.getName());
        try {
            Object o = p.getMain().newInstance();
            if (o instanceof IPlugin) {
                final IPlugin ip = (IPlugin) o;
                item.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        ip.start();
                    }
                });
            }
        } catch (InstantiationException ex) {
            Log.getLogger(PluginLoader.class).error("readPlugin:actionPerformed", ex);
        } catch (IllegalAccessException ex) {
            Log.getLogger(PluginLoader.class).error("readPlugin:actionPerformed", ex);
        }
        return item;
    }

    private JMenu getMenu(int id) {
        Plugin p = plugins.get(id);
        JMenu item = new JMenu(p.getName());
        try {
            Object o = p.getMain().newInstance();
            if (o instanceof IPlugin) {
                final IPlugin ip = (IPlugin) o;
                item.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        ip.start();
                    }
                });
            }
        } catch (InstantiationException ex) {
            Log.getLogger(PluginLoader.class).error("readPlugin:actionPerformed", ex);
        } catch (IllegalAccessException ex) {
            Log.getLogger(PluginLoader.class).error("readPlugin:actionPerformed", ex);
        }
        return item;
    }

    private JMenuItem getMenuItem(int id) {
        Plugin p = plugins.get(id);
        JMenuItem item = new JMenuItem(p.getName());
        try {
            Object o = p.getMain().newInstance();
            if (o instanceof IPlugin) {
                final IPlugin ip = (IPlugin) o;
                item.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        ip.start();
                    }
                });
            }
        } catch (InstantiationException ex) {
            Log.getLogger(PluginLoader.class).error("readPlugin:actionPerformed", ex);
        } catch (IllegalAccessException ex) {
            Log.getLogger(PluginLoader.class).error("readPlugin:actionPerformed", ex);
        }
        return item;
    }

    /**
     * 
     * @param parent
     * @return
     */
    public boolean readPlugin() {
        File f = new File("plugin");
        if (!f.exists() || !f.isDirectory()) {
            f.mkdir();//假如plugin File不存在，或者不是文件夹时，创建该文件夹
        }
        ArrayList<File> files = new ArrayList<File>();
        FileUtil.listFile(f, files);//支持将插件放到各自的文件夹中 03.20
        int n = files.size();
        for (int i = 0; i < n; i++) {
            try {
                String path = files.get(i).getAbsolutePath();
                System.out.println(path);
                addURL(new File(path).toURI().toURL());
                Plugin plugin = getClass(path);
                if (plugin != null) {
                    plugins.add(plugin);
                }
            } catch (java.io.FileNotFoundException ee) {
                continue;
            } catch (MalformedURLException ee) {
                continue;
            } catch (IOException ee) {
                continue;
            }


        }
        System.out.println("有效插件数量:" + getPluginNum());
        for (int i = 0; i < getPluginNum(); i++) {
            if (isPluginType(i, Plugin.SIMITEM)) {
                simPlugins.add(getMenuItem(i));
            }
            if (isPluginType(i, Plugin.HELPITEM)) {

                helpPlugins.add(getMenuItem(i));
            }
            if (isPluginType(i, Plugin.MENU)) {

                menuPlugins.add(getMenu(i));
            }
            if (isPluginType(i, Plugin.PLUGINITEM)) {

                pluginPlugins.add(getMenuItem(i));
            }
            if (isPluginType(i, Plugin.TOOLITEM)) {

                toolPlugins.add(getMenuItem(i));
            }
            if (isPluginType(i, Plugin.TOOLBUTTON)) {

                toolBarPlugins.add(getButton(i));
            }
            if (isPluginType(i, Plugin.SERVICE)) {

                servicePluginsNum++;
            }
        }
        System.out.println("模拟菜单项插件数量:" + getSimPluginNum());
        System.out.println("工具菜单项插件数量:" + getToolPluginNum());
        System.out.println("帮助菜单项插件数量:" + getHelpPluginNum());
        System.out.println("菜单插件数量:" + getMenuPluginNum());
        System.out.println("服务插件数量:" + servicePluginsNum);
        System.out.println("工具栏按钮插件数量:" + getToolBarPluginNum());

        return true;
    }
    private int servicePluginsNum = 0;

    /**
     * 
     * @return
     */
    public int getServicePluginsNum() {
        return servicePluginsNum;
    }

    /**
     * 
     * @return
     */
    public int getSimPluginNum() {
        return simPlugins.size();
    }

    /**
     * 
     * @return
     */
    public int getToolPluginNum() {
        return toolPlugins.size();
    }

    /**
     * 
     * @return
     */
    public int getPluginPluginNum() {
        return pluginPlugins.size();
    }

    /**
     * 
     * @return
     */
    public int getHelpPluginNum() {
        return helpPlugins.size();
    }

    /**
     * 
     * @return
     */
    public int getToolBarPluginNum() {
        return toolBarPlugins.size();
    }

    /**
     * 
     * @return
     */
    public int getMenuPluginNum() {
        return menuPlugins.size();
    }
    private int temp = 0;
    private ArrayList<JMenuItem> simPlugins = new ArrayList<JMenuItem>();
    private ArrayList<JMenuItem> toolPlugins = new ArrayList<JMenuItem>();

    /**
     * 
     * @return
     */
    public ArrayList<JMenuItem> getHelpPlugins() {
        return helpPlugins;
    }

    /**
     * 
     * @return
     */
    public ArrayList<JMenu> getMenuPlugins() {
        return menuPlugins;
    }

    /**
     * 
     * @return
     */
    public ArrayList<JMenuItem> getPluginPlugins() {
        return pluginPlugins;
    }

    /**
     * 
     * @return
     */
    public ArrayList<JButton> getToolBarPlugins() {
        return toolBarPlugins;
    }

    /**
     * 
     * @return
     */
    public ArrayList<JMenuItem> getToolPlugins() {
        return toolPlugins;
    }
    private ArrayList<JMenuItem> pluginPlugins = new ArrayList<JMenuItem>();
    private ArrayList<JMenuItem> helpPlugins = new ArrayList<JMenuItem>();
    private ArrayList<JButton> toolBarPlugins = new ArrayList<JButton>();
    private ArrayList<JMenu> menuPlugins = new ArrayList<JMenu>();
    private ArrayList<Plugin> plugins = new ArrayList<Plugin>();

    /**
     * 
     * @return
     */
    public ArrayList<JMenuItem> getSimPlugins() {
        return simPlugins;
    }

    /**
     * 
     * @return
     */
    public ArrayList<Plugin> getPlugins() {
        return plugins;
    }

    private Plugin getClass(String jar) throws IOException {
        JarFile file = new JarFile(jar);
        if (file.getManifest() != null) {
//            System.out.println("读取清单文件...");
            Attributes attr = file.getManifest().getMainAttributes();
            Plugin plugin = new Plugin();
            plugin.setId(temp++);
            if (attr.getValue("Plugin-Description") != null) {
                plugin.setDescription(attr.getValue("Plugin-Description"));
            } else {
                plugin.setDescription("未命名描述");
            }
            if (attr.getValue("Plugin-Version") != null) {
                plugin.setVersion(attr.getValue("Plugin-Version"));
            } else {
                plugin.setVersion("未命名版本");
            }
            if (attr.getValue("Plugin-Name") != null) {
//                System.out.println(attr.getValue("Plugin-Name"));
                plugin.setName(attr.getValue("Plugin-Name"));
            } else {
                plugin.setName("未命名插件");
            }
            if (attr.getValue("Plugin-Author") != null) {
//                System.out.println(attr.getValue("Plugin-Author"));
                plugin.setAuthor(attr.getValue("Plugin-Author"));
            } else {
                plugin.setAuthor("未命名作者");
            }
            if (attr.getValue("Plugin-View") != null) {
                plugin.setView(Integer.parseInt(attr.getValue("Plugin-View")));
            }
            if (attr.getValue("Plugin-Main") != null) {
                try {
                    plugin.setMain(findClass(attr.getValue("Plugin-Main")));
                } catch (ClassNotFoundException ex) {
                    Log.getLogger(PluginLoader.class).error("getClass:" + jar, ex);
                }
            }
            if (plugin.getView() != -1) {//插件类型为必要标示
                return plugin;
            }
        } else {
//            System.out.println("找不到清单文件！");
        }
        file.close();
        return null;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class c = findLoadedClass(name);
        if (c != null) {
            System.out.println("类已加载过");
            return new PluginLoader().loadClass(name);
        }
        return super.findClass(name);
    }

    //插件类型
    private boolean isPluginType(int id, int view) {

        Plugin p = plugins.get(id);
        if (p.getView() == view) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    public static PluginLoader getInstance() {
        if (instance == null) {
            instance = new PluginLoader();
        }
        return instance;
    }
}
