package com.diamond.colamovie.net.down.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.diamond.colamovie.net.down.M3U8;
import com.diamond.colamovie.net.down.M3U8Ts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 下午5:39
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class FileUtils {

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 生成本地m3u8索引文件，ts切片和m3u8文件放在相同目录下即可
     *
     * @param m3u8Dir
     * @param m3U8
     */
    public static File createLocalM3U8(File m3u8Dir, String fileName, M3U8 m3U8) throws IOException {
        return createLocalM3U8(m3u8Dir, fileName, m3U8, null);
    }

    /**
     * 生成AES-128加密本地m3u8索引文件，ts切片和m3u8文件放在相同目录下即可
     *
     * @param m3u8Dir
     * @param m3U8
     */
    public static File createLocalM3U8(File m3u8Dir, String fileName, M3U8 m3U8, String keyPath) throws IOException {
        File m3u8File = new File(m3u8Dir, fileName);
        BufferedWriter bfw = new BufferedWriter(new FileWriter(m3u8File, false));
        bfw.write("#EXTM3U\n");
        bfw.write("#EXT-X-VERSION:3\n");
        bfw.write("#EXT-X-MEDIA-SEQUENCE:0\n");
        bfw.write("#EXT-X-TARGETDURATION:13\n");
        for (M3U8Ts m3U8Ts : m3U8.tsList) {
            if (keyPath != null) bfw.write("#EXT-X-KEY:METHOD=AES-128,URI=\"" + keyPath + "\"\n");
            bfw.write("#EXTINF:" + m3U8Ts.seconds + ",\n");
            bfw.write(m3u8Dir.getPath() + File.separator + m3U8Ts.getFileName());
            bfw.newLine();
        }
        bfw.write("#EXT-X-ENDLIST");
        bfw.flush();
        bfw.close();
        return m3u8File;
    }
}
