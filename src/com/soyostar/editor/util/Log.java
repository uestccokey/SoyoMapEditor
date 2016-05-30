/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.util;

import org.apache.log4j.Logger;

/**
 *
 * @author 日志类
 */
public class Log {

    /**
     * 
     * @param cla
     * @return
     */
    public static Logger getLogger(Class cla) {
        return Logger.getLogger(cla.getName());
    }
}
