package com.diamond.colamovie.net;

import android.content.Context;
import android.util.Log;

import com.google.android.exoplayer2.offline.Downloader;

import chuangyuan.ycj.videolibrary.factory.JDefaultDataSourceFactory;
import chuangyuan.ycj.videolibrary.offline.DefaultProgressDownloader;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/22 下午12:44
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/22      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class DownLoad {

    DefaultProgressDownloader downloader;

    /***
     * 自定义下载
     * ***/
    public void customDwon(Context ctx,String url) {
        //设置下载缓存实例
        downloader = new DefaultProgressDownloader.Builder(ctx)
                .setMaxCacheSize(100000000)
                //设置你缓存目录
                .setCacheFileDir(ctx.getExternalCacheDir().getAbsolutePath())
                //缓存文件加密,那么在使用AES / CBC的文件系统中缓存密钥将被加密  密钥必须是16字节长.
                .setSecretKey("1234567887654321".getBytes())
                .setUri(url)
                //设置下载数据加载工厂类
                .setHttpDataSource(new JDefaultDataSourceFactory(ctx))
                .build();
        downloader.init();
        if ((int) downloader.getDownloadPercentage() == 100) {
            Log.d("_____", "downloadPercentage:" + (int) downloader.getDownloadPercentage());
        } else {
            downloader.download(new Downloader.ProgressListener() {
                @Override
                public void onDownloadProgress(Downloader downloader, float downloadPercentage, long downloadedBytes) {
                    Log.d("_____", "downloadPercentage:" + downloadPercentage + "downloadedBytes:" + downloadedBytes);
                }
            });
        }
    }
}
