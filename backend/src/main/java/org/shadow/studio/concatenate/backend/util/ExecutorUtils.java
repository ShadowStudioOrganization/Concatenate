package org.shadow.studio.concatenate.backend.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.shadow.studio.concatenate.backend.download.DownloadUtil;

public class ExecutorUtils {
    public static Boolean threadPollDownload(String[] urls, int threadSize) {

        ExecutorService threadPoll = Executors.newFixedThreadPool(threadSize);
        for (String url: urls) {
            long start = System.currentTimeMillis();
            Runnable runnable = () -> {
                try {
                    System.out.println("正在下线 from:" + url);
                    downloadFromUrl(url);
                    System.out.println("下载线程:" + Thread.currentThread().getName() + "执行完成.");
                    long end = System.currentTimeMillis();
                    System.out.println("耗时:"+ (double)(end-start) /1000 + "s");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
            threadPoll.submit(runnable);
        }
        threadPoll.shutdown();
        return true;
    }

    public static void downloadFromUrl(String url) throws IOException {
        String filename = DownloadUtil.INSTANCE.getUrlFlieName(url);
        downloadFromUrl(url,filename);
    }
    public static void downloadFromUrl(String url, String filename) throws IOException {
        URL Url = new URL(url);
        URLConnection connection = Url.openConnection();
        InputStream input = connection.getInputStream();
        OutputStream output = new FileOutputStream(filename);
        System.out.println("正在下载:" + filename);
        long start = System.currentTimeMillis();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        long end = System.currentTimeMillis();
        System.out.println(filename + "下载完成.耗时:" + (double)(end-start) /1000 + "s");
        input.close();
        output.close();
    }
}
