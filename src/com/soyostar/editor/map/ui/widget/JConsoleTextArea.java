/*
 * Copyright 2010-2011 Soyostar Software, Inc. All rights reserved.
 */
package com.soyostar.editor.map.ui.widget;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.soyostar.editor.util.LoopedStreams;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 *
 * @author Administrator
 */
public class JConsoleTextArea extends JTextArea {

    /**
     *
     * @param inStreams
     */
    public JConsoleTextArea(InputStream[] inStreams) {
        for (int i = 0; i < inStreams.length; ++i) {
            startConsoleReaderThread(inStreams[i]);
        }
    } // ConsoleTextArea()

    /**
     *
     * @throws IOException
     */
    public JConsoleTextArea() throws IOException {
        final LoopedStreams ls = new LoopedStreams();
        // �ض���System.out��System.err
        PrintStream ps = new PrintStream(ls.getOutputStream());
        System.setOut(ps);
        System.setErr(ps);

        startConsoleReaderThread(ls.getInputStream());
    } // ConsoleTextArea()

    private void startConsoleReaderThread(
            InputStream inStream) {
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(inStream));
        new Thread(new Runnable() {

            public void run() {
                StringBuffer sb = new StringBuffer();
//                append("��ʱ�䣺" + SoftData.startTime + "\n");
                try {
                    String s;
                    Document doc = getDocument();
                    while ((s = br.readLine()) != null) {
                        boolean caretAtEnd = false;
                        caretAtEnd = getCaretPosition() == doc.getLength()
                                ? true : false;
                        sb.setLength(0);
                        append(sb.append(s).append("\n").toString());
                        if (caretAtEnd) {
                            setCaretPosition(doc.getLength());
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "��BufferedReader��ȡ����" + e);
                    System.exit(1);
                }
            }
        }).start();
    } // startConsoleReaderThread()
} // ConsoleTextArea