/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;

/**
 *
 * @author Administrator
 */
public class LoopedStreams {

    private PipedOutputStream pipedOS =
            new PipedOutputStream();
    private boolean keepRunning = true;
    private ByteArrayOutputStream byteArrayOS =
            new ByteArrayOutputStream() {

        @Override
                public void close() {
                    keepRunning = false;
                    try {
                        super.close();
                        pipedOS.close();
                    } catch (IOException e) {
// 记录错误或其他处琄1�7
// 为简单计，此处我们直接结杄1�7
                        System.exit(1);
                    }
                }
            };
    private PipedInputStream pipedIS = new PipedInputStream() {

        @Override
        public void close() {
            keepRunning = false;
            try {
                super.close();
            } catch (IOException e) {
// 记录错误或其他处琄1�7
// 为简单计，此处我们直接结杄1�7
                System.exit(1);
            }
        }
    };

    /**
     *
     * @throws IOException
     */
    public LoopedStreams() throws IOException {
        pipedOS.connect(pipedIS);
        startByteArrayReaderThread();
    } // LoopedStreams()

    /**
     *
     * @return
     */
    public InputStream getInputStream() {
        return pipedIS;
    } // getInputStream()

    /**
     *
     * @return
     */
    public OutputStream getOutputStream() {
        return byteArrayOS;
    } // getOutputStream()

    private void startByteArrayReaderThread() {
        new Thread(new Runnable() {

            public void run() {
                while (keepRunning) {
// 棄1�7查流里面的字节数
                    if (byteArrayOS.size() > 0) {
                        byte[] buffer = null;
                        synchronized (byteArrayOS) {
                            buffer = byteArrayOS.toByteArray();
                            byteArrayOS.reset(); // 清除缓冲匄1�7
                        }
                        try {
// 把提取到的数据发送给PipedOutputStream
                            pipedOS.write(buffer, 0, buffer.length);
                        } catch (IOException e) {
// 记录错误或其他处琄1�7
// 为简单计，此处我们直接结杄1�7
                            System.exit(1);
                        }
                    } else // 没有数据可用，线程进入睡眠状怄1�7
                    {
                        try {
// 每隔1秒查看ByteArrayOutputStream棄1�7查新数据
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }).start();
    } // startByteArrayReaderThread()
} // LoopedStreams