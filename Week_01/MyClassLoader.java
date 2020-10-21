package com.day1;

import java.io.*;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            (new MyClassLoader()).hello();
        } catch (Exception var3) {
            String var2 = var3.getMessage();
        }
        System.out.println("我进来了");
    }

    public void hello() throws IOException {
        try {
            byte[] bytes = readFileToByte("E:\\Hello.xlass");
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
            String className = "Hello";
            Class c = this.defineClass(className, bytes, 0, bytes.length);
            for (Method m : c.getMethods()) {
                System.out.println("方法名：" + m.getName());
            }
            for (java.lang.reflect.Field m : c.getFields()) {
                System.out.println("字段名名：" + m.getName());
            }
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
    }

    private byte[] readFileToByte(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    private byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

}
