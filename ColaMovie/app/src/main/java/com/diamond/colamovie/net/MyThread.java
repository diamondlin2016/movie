package com.diamond.colamovie.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/17 下午1:50
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/17      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class MyThread<T> extends Thread {

    private final IDataListener<T> listener;

    public MyThread(IDataListener<T> listener) {
        this.listener = listener;
    }

    void onFinish(final T pageBean) {
        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (null != pageBean) {
                    listener.onSuccess(pageBean);
                } else {
                    listener.onFail(0, "解析错误");
//                    ToastUtil.show(UrlApi.HOME + "解析错误");
                }
            }
        }.sendEmptyMessage(0);
    }
}
