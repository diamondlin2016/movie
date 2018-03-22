package com.diamond.colamovie.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.diamond.colamovie.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/19 下午10:51
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/19      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public abstract class BaseActivity extends AppCompatActivity {
    private AlertDialog pd;
    protected final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            getArguments(getIntent());
        }
//        PushAgent.getInstance(this).onAppStart();
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        setViewComponent();
    }

    protected void setStateBarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected void initProgressDialog() {
        if (pd == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AlertDialog_Transparent);
            View inflate = View.inflate(this, R.layout.dialog_progress, null);
            builder.setView(inflate);
            builder.setCancelable(false);
            pd = builder.create();
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    protected void showProgressDialog() {
        if (pd == null) {
            initProgressDialog();
        }
        pd.show();
        WindowManager.LayoutParams params = pd.getWindow().getAttributes();
        params.width = 400;
        params.height = 300;
        pd.getWindow().setAttributes(params);
    }

    protected void dismissProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    protected void getArguments(Intent intent) {

    }

    protected void setViewComponent() {

    }

    protected abstract int getLayoutResource();

    protected void requestAppPermissions(String[] permissions, MultiplePermissionsListener listener) {
        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(listener)
                .check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }
}
