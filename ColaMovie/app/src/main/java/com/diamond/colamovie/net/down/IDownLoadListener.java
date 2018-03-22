package com.diamond.colamovie.net.down;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 下午5:43
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public interface IDownLoadListener {
    void onSuccess();
    void onFail(String msg);
    void onProgress(int progress);
    void onStartDown();
}
