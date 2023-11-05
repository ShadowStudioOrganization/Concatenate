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
    public static Boolean threadPollDownload(String[] urls) throws InterruptedException, IOException {

        ExecutorService threadPoll = Executors.newFixedThreadPool(10);
//        Thread.sleep(1000);
        for (String url: urls) {
            long start = System.currentTimeMillis();
            downloadFromUrl(url);
            threadPoll.execute(() -> {
                System.out.println("下载线程:" + Thread.currentThread().getName() + "执行完成.");
            });
            long end = System.currentTimeMillis();
            System.out.println("耗时:"+ (end-start)/1000 + "s");
        }
        threadPoll.shutdown();
        return true;
    }

    public static void downloadFromUrl(String url) throws IOException {
        URL Url = new URL(url);
        URLConnection connection = Url.openConnection();
        InputStream input = connection.getInputStream();
        OutputStream output = new FileOutputStream(DownloadUtil.INSTANCE.getUrlFlieName(url));
        System.out.println("正在下载:" + DownloadUtil.INSTANCE.getUrlFlieName(url));
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        input.close();
        output.close();
    }
    public static void downloadFromUrl(String url, String filename) throws IOException {
        URL Url = new URL(url);
        URLConnection connection = Url.openConnection();
        InputStream input = connection.getInputStream();
        OutputStream output = new FileOutputStream(filename);
        System.out.println("正在下载:" + filename);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        input.close();
        output.close();
    }
}
