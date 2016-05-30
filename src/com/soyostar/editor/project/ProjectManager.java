/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.project;

import com.soyostar.editor.map.main.AppData;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.SAXReader;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class ProjectManager {

    private ProjectManager() {
    }

    /**
     *
     * @return
     */
    public static boolean create() {
        boolean returnValue = false;
        Document doc = DocumentHelper.createDocument();
        Element project = doc.addElement("Project");
        project.addElement("softVersion").addText(AppData.getInstance().getCurProject().getSoftVersion());
        project.addElement("projectName").addText(AppData.getInstance().getCurProject().getName());

        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter xmlw = new XMLWriter(new FileWriter(AppData.getInstance().getCurProject().getPath() + File.separator + "Project.xml"), format);
            xmlw.write(doc);
            xmlw.close();
//            System.out.println(doc.asXML());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /**
     *
     * @return
     */
    public static boolean get() {
        boolean returnValue = false;//操作结果
        SAXReader sax = new SAXReader();
        try {
            System.out.println("************************************");
            System.out.println("读取项目文件...");
            System.out.println("项目路径:" + AppData.getInstance().getCurProject().getPath());
            Document document = sax.read(new File(AppData.getInstance().getCurProject().getPath() + File.separator + "Project.xml"));
            // 得到根元素
            Element root = document.getRootElement();
            // 枚举节点
            for (Iterator i = root.elementIterator("projectName"); i.hasNext();) {
                Element name = (Element) i.next();
                AppData.getInstance().getCurProject().setName(name.getText());
            }
            for (Iterator i = root.elementIterator("softVersion"); i.hasNext();) {
                Element version = (Element) i.next();
                AppData.getInstance().getCurProject().setSoftVersion(version.getText());
            }
            returnValue = true;
            System.out.println("项目名:" + AppData.getInstance().getCurProject().getName());
            System.out.println("创建版本:" + AppData.getInstance().getCurProject().getSoftVersion());
            System.out.println("读取项目文件成功!");
            System.out.println("************************************");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return returnValue;
    }
}
