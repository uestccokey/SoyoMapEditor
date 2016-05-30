/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.plugin;

/**
 *
 * @author Administrator
 */
public class Plugin {

    private int id;             //标示

    /**
     * 
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     */
    public Class getMain() {
        return main;
    }

    /**
     * 
     * @param main
     */
    public void setMain(Class main) {
        this.main = main;
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
    }

    /**
     * 
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }
    private Class main;         //入口
    private String name;        //名称
    private String author;      //作者
    private String description; //描述
    private String version;     //版本
    private int view = -1;      //功能

    /**
     * 
     * @return
     */
    public int getView() {
        return view;
    }

    /**
     * 
     * @param view
     */
    public void setView(int view) {
        this.view = view;
    }
    /**
     * 
     */
    public static final int SERVICE = 0;    //服务
    /**
     * 
     */
    public static final int MENU = 1;       //菜单
    /**
     * 
     */
    public static final int TOOLITEM = 2;   //工具菜单菜单项
    /**
     * 
     */
    public static final int PLUGINITEM = 3; //插件菜单菜单项
    /**
     * 
     */
    public static final int SIMITEM = 4;    //模拟菜单菜单项
    /**
     * 
     */
    public static final int HELPITEM = 5;   //帮助菜单菜单项
    /**
     * 
     */
    public static final int TOOLBUTTON = 6; //工具栏按钮
}
