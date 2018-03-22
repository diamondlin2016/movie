package com.diamond.colamovie.net.down;

import com.diamond.colamovie.MyApplication;
import com.diamond.colamovie.net.down.utils.FileUtils;
import com.diamond.colamovie.net.down.utils.MD5Utils;
import com.diamond.colamovie.utils.ToastHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 下午5:23
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class M3U8DownManager {

    public static ArrayList<DownTask> mDownList = new ArrayList<>();
    public static String BASE_CACHE_URL = FileUtils.getCacheDirectory(MyApplication.getContext(), true) + File.separator + "ColaMovie/";

    public static boolean down(String url, IDownLoadListener loadListener) {
        DownTask downTask1 = getsDownTaskByUrl(url);
        if (downTask1 != null && !downTask1.needStop) {
            downTask1.listener = loadListener;
            return true;
        }
        if (mDownList.size()>=3){
            ToastHelper.showToastMessage("最多只能同时下载三个");
            return false;
        }
        DownTask downTask = new DownTask(url, loadListener);
        downTask.start();
        mDownList.add(downTask);
        return true;
    }

    public static void stop(String url) {
        DownTask downTask = getsDownTaskByUrl(url);
        if (downTask != null) {
            downTask.stopTask();
            mDownList.remove(downTask);
        }
    }

    private static DownTask getsDownTaskByUrl(String url) {
        for (DownTask task : mDownList) {
            if (task.url.equals(url)) {
                return task;
            }
        }
        return null;
    }

    //删除某个电影
    public static void clearCacheByUrl(String url) {
        clearCache(new File(BASE_CACHE_URL + MD5Utils.encode(url)));
    }

    public static void clearCache() {
        clearCache(new File(BASE_CACHE_URL));
    }

    private static boolean clearCache(File dir) {
        /**
         * 清空文件夹
         */
        if (dir.exists()) {// 判断文件是否存在
            if (dir.isFile()) {// 判断是否是文件
                return dir.delete();// 删除文件
            } else if (dir.isDirectory()) {// 否则如果它是一个目录
                File[] files = dir.listFiles();// 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
                    clearCache(files[i]);// 把每个文件用这个方法进行迭代
                }
                return dir.delete();// 删除文件夹
            }
        }
        return true;
    }


}
