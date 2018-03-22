package com.diamond.colamovie.fragment;

import android.content.pm.PackageManager;
import android.widget.TextView;

import com.diamond.colamovie.R;
import com.diamond.colamovie.activity.CacheActivity;
import com.diamond.colamovie.activity.FeedbackActivity;
import com.diamond.colamovie.base.BaseViewPagerFragment;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/19 下午11:06
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/19      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class PersonFragment extends BaseViewPagerFragment {
    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @Override
    protected void setUpViewComponent() {
        String version = "1.0.0";
        try {
            version = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mTvVersion.setText(String.format(getString(R.string.text_version_string), new Date(System.currentTimeMillis()).getYear() + 1900, version));
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_person;
    }

    @OnClick(R.id.tv_cache)
    void jumpCache() {
        CacheActivity.startActivity(getContext());
    }

    @OnClick(R.id.tv_feedback)
    void feedback() {
        FeedbackActivity.startActivity(getActivity());

    }

    @OnClick(R.id.tv_about_me)
    void sponsor() {

    }

}
