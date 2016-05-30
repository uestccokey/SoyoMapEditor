/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soyostar.editor.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 * 
 * @author Administrator
 */
public class PngCompress {

    private static final byte[] PNG_HEAD = {(byte) 0x89, (byte) 0x50,
        (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A,
        (byte) 0x0A};
    /**
     * 
     */
    public long sizeBefore;
    /**
     * 
     */
    public long sizeAfter;

    /**
     * 
     * @param fileVector
     */
    public PngCompress(Vector<File> fileVector) {
        this.v = fileVector;
    }
    private Vector<File> v;

    /**
     * 
     * @throws Exception
     */
    public void compile() throws Exception {

        System.out.println("================begin compress==================");
        for (int i = 0, n = v.size(); i < n; i++) {
            treatFile(v.elementAt(i));
        }
        System.out.println("================end compress==================");
    }

    /**
     * 
     * @param file
     * @throws Exception
     */
    public void treatFile(File file) throws Exception {
        long fileLen = file.length();
        byte[] data = compileFile(file);
        sizeBefore += fileLen;
        if (data != null) {
            // good png
            if (file.canRead() && file.canWrite()) {
                if (file.delete()) {
                    if (file.createNewFile()) {
                        BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(file));
                        bos.write(data);
                        bos.close();
                        sizeAfter += data.length;
                        System.out.println("[" + file.getName() + "] compressed,"
                            + fileLen + "=>" + data.length);
                    } else {
                        System.out.println("can't recreate file:" + file.getName()
                            + ",please try again");
                    }
                } else {
                    System.out.println("can't delete file:" + file.getName()
                        + " skiped,please try again");
                }
            } else {
                System.out.println("未进行处理:" + file.getName());
            }
        } else {
            System.out.println("bad png file:" + file.getName() + ",skiped");
            sizeAfter += fileLen;
        }
    }

    /**
     * 
     * @param bytes
     * @return
     */
    public static int bytes2Int(byte[] bytes) {
        return bytes2Int(bytes, 0, bytes.length);
    }

    /**
     * 
     * @param bytes
     * @param offset
     * @param len
     * @return
     */
    public static int bytes2Int(byte[] bytes, int offset, int len) {
        int value = 0;
        for (int i = len - 1; i >= 0; i--) {
            int b = bytes[offset + i] & 0xff;
            value = (b << (8 * (len - 1 - i))) | value;
        }
        return value;
    }

    private byte[] compileFile(File pngFile) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RandomAccessFile raf = new RandomAccessFile(pngFile, "r");
        if (readAndCheckHead(raf)) {
            baos.write(PNG_HEAD);
            byte[] sizeData;
            byte[] typeData;
            while (true) {
                sizeData = new byte[4];
                if (raf.read(sizeData) != sizeData.length) {
                    break;
                }

                typeData = new byte[4];
                if (raf.read(typeData) != typeData.length) {
                    break;
                }
                String blockType = new String(typeData);
                int contentSize = bytes2Int(sizeData);

                if (blockType.equals("IHDR") || blockType.equals("PLTE")
                    || blockType.equals("IDAT") || blockType.equals("IEND")
                    || blockType.equals("tRNS")) {

                    // assert contentSize > 0;
                    byte[] content = new byte[contentSize];
                    if (raf.read(content) != contentSize) {
                        break;
                    }

                    byte[] crcData = new byte[4];
                    if (raf.read(crcData) != crcData.length) {
                        break;
                    }

                    baos.write(sizeData);
                    baos.write(typeData);
                    baos.write(content);
                    baos.write(crcData);
                    if (blockType.equals("IEND")) {
                        break;
                    }
                } else {
                    raf.skipBytes(contentSize + 4);
                }
            }

            raf.close();
            baos.close();
            return baos.toByteArray();
        } else {
            raf.close();
            baos.close();
            return null;
        }

    }

    private boolean readAndCheckHead(RandomAccessFile raf) throws Exception {
        byte[] head = new byte[8];
        if (raf.read(head) == head.length) {
            return byteArrayEquals(head, PNG_HEAD);
        } else {
            return false;
        }

    }

    /**
     * 
     * @param ba1
     * @param ba2
     * @return
     */
    public static boolean byteArrayEquals(byte[] ba1, byte[] ba2) {
        if (ba1 != null) {
            if (ba2 != null) {
                if (ba1.length == ba2.length) {
                    for (int i = 0; i < ba1.length; i++) {
                        if (ba1[i] != ba2[i]) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } else {
            return ba2 == null;
        }
    }
}
