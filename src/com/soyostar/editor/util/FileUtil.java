/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.util;

import com.soyostar.editor.project.Project;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FileUtil {

    /**
     *
     * @param destFileName
     * @return
     */
    public static boolean CreateFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
            return false;
        }
        if (!file.getParentFile().exists()) {
            System.out.println("目标文件所在路径不存在，准备创建。。。");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目录文件所在的目录失败！");
                return false;
            }
        }
        // 创建目标文件
        try {
            if (file.createNewFile()) {
                System.out.println("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                System.out.println("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建单个文件" + destFileName + "失败！");
            return false;
        }
    }

    /**
     * 
     * @param f
     * @param files
     */
    public static void listFile(File f, ArrayList<File> files) {
        if (f.isDirectory()) {
//            System.out.println("directory:" + f.getPath() + "--parent:" + f.getParent());
            IOFileFilterCreater jarFilter = new IOFileFilterCreater("jar");
            File[] t = f.listFiles(jarFilter);
            for (int i = 0; i < t.length; i++) {
                listFile(t[i], files);
            }
        } else {
            files.add(f);
//            System.out.println("fileName:" + f.getAbsolutePath() + "--parent:" + f.getParent());
        }
    }

    /**
     *
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        // 创建单个目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }
    // 复制文件

    /**
     * 
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile)
        throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);
        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);
        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();
        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }
    // 复制文件夹

    /**
     * 
     * @param sourceDir
     * @param targetDir
     */
    public static void copyDirectiory(String sourceDir, String targetDir) {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath()
                    + File.separator + file[i].getName());
                try {
                    copyFile(sourceFile, targetFile);
                } catch (IOException ex) {
                    System.err.println("复制res:" + file[i].getName() + "失败！");
                }
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + File.separatorChar + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + File.separatorChar + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     *
     * @param prefix
     * @param suffix
     * @param dirName
     * @return
     */
    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        try {
            if (dirName == null) {
                // 在默认文件夹下创建临时文件
                tempFile = File.createTempFile(prefix, suffix);
                return tempFile.getCanonicalPath();
            } else {
                File dir = new File(dirName);
                // 如果临时文件所在目录不存在，首先创建
                if (!dir.exists()) {
                    if (!FileUtil.createDir(dirName)) {
                        System.out.println("创建临时文件失败，不能创建临时文件所在目录！");
                        return null;
                    }
                }
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建临时文件失败" + e.getMessage());
            return null;
        }
    }
}
