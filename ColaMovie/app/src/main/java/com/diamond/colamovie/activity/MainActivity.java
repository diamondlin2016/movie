package com.diamond.colamovie.activity;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;

import com.diamond.colamovie.R;
import com.diamond.colamovie.base.BaseActivity;
import com.diamond.colamovie.base.BaseViewPagerAdapter;
import com.diamond.colamovie.base.BaseViewPagerFragment;
import com.diamond.colamovie.fragment.ColumnFragment;
import com.diamond.colamovie.fragment.HomeFragment;
import com.diamond.colamovie.fragment.PersonFragment;
import com.diamond.colamovie.model.ControlModel;
import com.diamond.colamovie.net.IDataListener;
import com.diamond.colamovie.net.JsoupApi;
import com.diamond.colamovie.widget.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IDataListener<ControlModel> {

    @BindView(R.id.view_pager)
    CustomViewPager mViewPager;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private ArrayList<BaseViewPagerFragment> mFragments;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0, false);
                    return true;
                case R.id.navigation_column:
                    mViewPager.setCurrentItem(1, false);
                    return true;
                case R.id.navigation_person:
                    mViewPager.setCurrentItem(2, false);
                    return true;
            }
            return false;
        }
    };


    private void createFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new ColumnFragment());
        mFragments.add(new PersonFragment());
    }

    @Override
    protected void setViewComponent() {
        setStateBarTransparent();
        createFragments();
        mViewPager.setOffscreenPageLimit(mFragments.size() - 1); //ViewPager的缓存页面数量
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), null, mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setScrollable(false);
        mViewPager.setCurrentItem(1, false);
        mNavigation.setSelectedItemId(R.id.navigation_column);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        JsoupApi.getInstance().checkVersion(this);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onSuccess(ControlModel controlModel) {
        int code = 1;
        try {
            code = getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (controlModel.versionCode >= code && !controlModel.canOpen) {
            cantOpen(controlModel.upData);
        }
    }

    private void cantOpen(String upData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setCancelable(false);
        if (TextUtils.isEmpty(upData)){
            builder.setMessage("当前版本不可用，请加 qq 群：703062136 获取最新版本");
        }else {
            builder.setMessage(upData);
        }
        builder.create().show();
    }

    @Override
    public void onFail(int errorCode, String errorMsg) {

    }
}
