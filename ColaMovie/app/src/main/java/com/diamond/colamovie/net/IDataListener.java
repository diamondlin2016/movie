package com.diamond.colamovie.net;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2017/11/28 下午5:06
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2017/11/28      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public interface IDataListener<T> {
    /**
     * @param t 响应参数
     */
    void onSuccess(T t);

    void onFail(int errorCode, String errorMsg);
}
