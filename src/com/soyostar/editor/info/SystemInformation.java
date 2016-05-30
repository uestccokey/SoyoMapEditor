/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.info;

import java.util.Calendar;

/**
 *
 * @author 系统信息
 */
public class SystemInformation {

    /**
     * 当前日期
     */
    public static class Date {

        private static final Calendar calCurrent = Calendar.getInstance();
        /**
         *
         */
        public static final int Day = calCurrent.get(Calendar.DATE);
        /**
         *
         */
        public static final int Month = calCurrent.get(Calendar.MONTH) + 1;
        /**
         *
         */
        public static final int Year = calCurrent.get(Calendar.YEAR);
    }
    /**
     * 操作系统名
     */
    public static final String osName = System.getProperty("os.name");
    /**
     * java运行时环境版本
     */
    public static final String javaVersion = System.getProperty("java.version");
    /**
     * java运行时环境供应商
     */
    public static final String javaVendor = System.getProperty("java.vendor");
    /**
     * java安装目录
     */
    public static final String javaHome = System.getProperty("java.home");
    /**
     * java虚拟机实现版本
     */
    public static final String javaVmVersion = System.getProperty("java.vm.version");
    /**
     * java类路径
     */
    public static final String javaClassPath = System.getProperty("java.class.path");
    /**
     * java默认的零时文件路径
     */
    public static final String javaIoTmpDir = System.getProperty("java.io.tmpdir");
    /**
     * 操作系统版本
     */
    public static final String osVersion = System.getProperty("os.version");
    /**
     * 操作系统架构
     */
    public static final String osArch = System.getProperty("os.arch");
    /**
     * 用户账户名称
     */
    public static final String userName = System.getProperty("user.name");
    /**
     * 用户主目录
     */
    public static final String userHome = System.getProperty("user.home");
    /**
     * 用户当前目录
     */
    public static final String userDir = System.getProperty("user.dir");
    /**
     * Java 虚拟机实现供应商
     */
    public static final String javaVmVendor = System.getProperty("java.vm.vendor");
    /**
     * 加载库时搜索的路径列表
     */
    public static final String javaLibraryPath = System.getProperty("java.library.path");

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        long time = System.currentTimeMillis();
        System.out.println("操作系统名:" + osName);
        System.out.println("java运行时环境版本:" + javaVersion);
        System.out.println("java运行时环境供应商:" + javaVendor);
        System.out.println("java安装目录:" + javaHome);
        System.out.println("java虚拟机实现版本:" + javaVmVersion);
        System.out.println("java类路径:" + javaClassPath);
        System.out.println("java默认的零时文件路径:" + javaIoTmpDir);
        System.out.println("操作系统版本:" + osVersion);
        System.out.println("操作系统架构:" + osArch);
        System.out.println("用户账户名称:" + userName);
        System.out.println("用户主目录:" + userHome);
        System.out.println("用户当前目录:" + userDir);
        System.out.println("Java 虚拟机实现供应商:" + javaVmVendor);
        System.out.println("加载库时搜索的路径列表:" + javaLibraryPath);
        System.out.println("usetime:" + (System.currentTimeMillis() - time) + "ms");
    }
}
