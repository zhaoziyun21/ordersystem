package com.kssj.jvm;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * 深入研究jvm底层机制，写了一个测试类一直加载class，
 * 		模拟加载类过多导致Perm永久代报：java.lang.OutOfMemoryError：PermGen space 内存溢出
 * 
 * @author zhangxuejiao
 *
 */
public class TestPermGen {

    private static List<Object> insList = new ArrayList<Object>();

    /**
     * 一直加载log日志jar下的类，直到发生永久带溢出
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        permLeak();
    }

    private static void permLeak() throws Exception {
        for (int i = 0; i < 10000; i++) {
            URL[] urls = getURLS();
            URLClassLoader urlClassloader = new URLClassLoader(urls, null);
            Class<?> logfClass = Class.forName("org.apache.commons.logging.LogFactory", true, urlClassloader);
            Method getLog = logfClass.getMethod("getLog", String.class);
            Object result = getLog.invoke(logfClass, "TestPermGen");
            insList.add(result);
            System.out.println(i + ": " + result);
        }
    }

    private static URL[] getURLS() throws MalformedURLException {
        File libDir = new File("E:/develop/loanRepository/commons-logging/commons-logging/1.1.1");
        File[] subFiles = libDir.listFiles();
        int count = subFiles.length;
        URL[] urls = new URL[count];
        for (int i = 0; i < count; i++) {
            urls[i] = subFiles[i].toURI().toURL();
        }
        return urls;
    }

}
