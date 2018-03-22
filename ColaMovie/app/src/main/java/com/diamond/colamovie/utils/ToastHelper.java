package com.diamond.colamovie.utils;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.diamond.colamovie.MyApplication;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/23 下午5:55
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/23      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class ToastHelper {
    public static void showToastMessage(String msg) {
        showToastMessage(msg, Toast.LENGTH_SHORT);
    }

    public static void showToastMessage(String msg, int length) {
        if (!TextUtils.isEmpty(msg)) {
//            // create instance
//            Toast toast = new Toast(MyApplication.getContext());
//            // set duration
//            toast.setDuration(length);
//            // set position
//            toast.show();
            Toast.makeText(MyApplication.getContext(), msg, length).show();


        }
    }

    public static void showToastMessage(@StringRes int resId) {
        showToastMessage(resId, Toast.LENGTH_SHORT);
    }

    public static void showToastMessage(@StringRes int resId, int length) {
        if (resId != 0) {
//            // create instance
//            Toast toast = new Toast(MyApplication.getContext());
//            // set duration
//            toast.setDuration(length);
//            // set position
//            toast.show();
            Toast.makeText(MyApplication.getContext(), resId, length).show();
        }
    }


    public static void showSnackBarMessage(String msg, View view) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

}
