package com.diamond.colamovie.net.down;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.diamond.colamovie.net.down.utils.FileUtils;
import com.diamond.colamovie.net.down.utils.MD5Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.diamond.colamovie.net.down.M3U8DownManager.BASE_CACHE_URL;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 下午5:41
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class DownTask extends Thread {

    public final String url;
    public IDownLoadListener listener;
    private ExecutorService mExecutorService;//负责下载 ts 列表 的线程池
    private int mTotalTs;//总长度
    private int mCurTs;//当前下载 ts 进度
    private boolean isStartDownload;//是否正在下载
    public boolean needStop;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DownTask(String url, IDownLoadListener loadListener) {
        this.url = url;
        this.listener = loadListener;
    }

    @Override
    public void run() {
        super.run();
        M3U8 m3U8Info = null;
        try {
            m3U8Info = M3U8InfoUtils.getM3U8Info(url);
        } catch (IOException e) {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onFail("资源已失效");
                }
            });
            needStop = true;
            return;
        }
        String saveDir = BASE_CACHE_URL + MD5Utils.encode(url);

        final File dir = new File(saveDir);
        //没有就创建
        if (!dir.exists()) {
            dir.mkdirs();
        }

        mTotalTs = m3U8Info.tsList.size();
        mCurTs = 1;

        mExecutorService = Executors.newFixedThreadPool(3);

        isStartDownload = true;

        final String basePath = m3U8Info.basePath;

        for (final M3U8Ts m3U8Ts : m3U8Info.tsList) {//循环下载
            if (needStop) {
                break;
            }

            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {

                    File file;
                    file = new File(dir + File.separator + m3U8Ts.getFileName());

                    if (!file.exists()) {//下载过的就不管了

                        FileOutputStream fos = null;
                        InputStream inputStream = null;
                        try {
                            URL url = new URL(basePath + m3U8Ts.file);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(20 * 1000);
                            conn.setReadTimeout(200 * 1000);
                            if (conn.getResponseCode() == 200) {
                                if (isStartDownload) {
                                    isStartDownload = false;
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onStartDown();
                                        }
                                    });
                                }
                                inputStream = conn.getInputStream();
                                fos = new FileOutputStream(file);//会自动创建文件
                                int len = 0;
                                byte[] buf = new byte[8 * 1024 * 1024];
                                while ((len = inputStream.read(buf)) != -1) {
                                    fos.write(buf, 0, len);//写入流中
                                }
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onFail("下载失败");
                                        needStop = true;
                                    }
                                });
                            }
                        } catch (final Exception e) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onFail(e.getMessage());
                                    needStop = true;
                                }
                            });
                        } finally {//关流
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                }
                            }
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                }
                            }
                        }

                    }
                    mCurTs++;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onProgress(mCurTs * 100 / mTotalTs);
                        }
                    });
                }
            });
        }
        mExecutorService.shutdown();
        while (!mExecutorService.isTerminated()) {
            try {
                Thread.sleep(1000);
                Log.e("___", "在睡觉");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //生成本地 m3u8 索引文件
        File m3u8File = null;
        try {
            m3u8File = FileUtils.createLocalM3U8(new File(saveDir), "local.m3u8", m3U8Info);
            Log.e("____", m3u8File.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        m3U8Info.m3u8FilePath = m3u8File.getPath();
        m3U8Info.dirFilePath = saveDir;
        needStop = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess();
            }
        });



    }

    public void stopTask() {
        if (mExecutorService != null) {
            mExecutorService.shutdown();
            needStop = true;
        }
    }
}
