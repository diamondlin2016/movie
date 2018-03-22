package com.diamond.colamovie.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import com.diamond.colamovie.R;
import com.diamond.colamovie.base.BaseActivity;
import com.diamond.colamovie.utils.ToastHelper;

import butterknife.OnClick;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/31 下午3:02
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/31      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class FeedbackActivity extends BaseActivity {

    private ClipboardManager mClipboardManager;

    public static void startActivity(Context ctx) {
        Intent intent = new Intent(ctx, FeedbackActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void setViewComponent() {
        super.setViewComponent();
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @OnClick(R.id.bt_email)
    void copyEmail(){
        mClipboardManager.setPrimaryClip(ClipData.newPlainText("email", "anonymous_lin@foxmail.com"));
        ToastHelper.showSnackBarMessage("邮箱地址复制成功",findViewById(android.R.id.content));
    }

    @OnClick(R.id.bt_wechat)
    void copyWechat(){
        mClipboardManager.setPrimaryClip(ClipData.newPlainText("email", "moco10"));
        ToastHelper.showSnackBarMessage("复制成功，赶快去加好友吧",findViewById(android.R.id.content));
    }

    @OnClick(R.id.bt_qq)
    void copyQQ(){
        mClipboardManager.setPrimaryClip(ClipData.newPlainText("email", "703062136"));
        ToastHelper.showSnackBarMessage("复制成功，赶快去加群吧",findViewById(android.R.id.content));
    }


}
