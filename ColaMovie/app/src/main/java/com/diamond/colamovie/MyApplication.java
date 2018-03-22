package com.diamond.colamovie;

import android.app.Application;
import android.content.Context;

import com.diamond.colamovie.contact.AppSpContact;
import com.diamond.colamovie.utils.SharedPreferencesHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;


/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/22 下午4:03
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/22      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        SharedPreferencesHelper.getInstance().Builder(this);
        initUmeng();
    }

    private void initUmeng() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, AppSpContact.UMENG_KEY);
        UMConfigure.setLogEnabled(true);

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回device token
//                Log.e("MyApplication:－－－－－－－－",deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                Log.e("MyApplication:－－－－－－－－－",s+"......"+s1);
//            }
//        });

    }

    public static Context getContext() {
        return mContext;
    }

}
