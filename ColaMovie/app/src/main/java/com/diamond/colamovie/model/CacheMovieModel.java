package com.diamond.colamovie.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.diamond.colamovie.contact.AppSpContact;
import com.diamond.colamovie.utils.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/22 下午5:24
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/22      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class CacheMovieModel {


    public static ArrayList<CacheBean> getCacheList() {
        ArrayList<CacheBean> list = new ArrayList<>();
        String json = SharedPreferencesHelper.getInstance().getString(AppSpContact.CACHE_MOVIE);
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(CacheBean.getBeanFromJsonObject(jsonArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static CacheBean findByUrl(String url) {
        for (CacheBean bean : getCacheList()) {
            if (bean.playUrl.equals(url))
                return bean;
        }
        return null;
    }

    public static synchronized void putACacheBean(@NonNull CacheBean bean) {
        ArrayList<CacheBean> list = getCacheList();
        for (CacheBean b : list) {
            if (b.playUrl.equals(bean.playUrl)) {
                b.name = bean.name;
                b.img = bean.img;
                b.isSuccess = bean.isSuccess;
                bean = null;
                break;
            }
        }
        if (null != bean) {
            list.add(bean);
        }
        putList(list);

    }

    private static void putList(ArrayList<CacheBean> list) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (int i = 0; i < list.size(); i++) {
            CacheBean cacheBean = list.get(i);
            buffer.append(cacheBean.toJson());
            if (i != list.size() - 1) {
                buffer.append(",");
            }
        }
        buffer.append("]");
        SharedPreferencesHelper.getInstance().putString(AppSpContact.CACHE_MOVIE, buffer.toString());
    }

    public static void deleteByUrl(String url) {
        ArrayList<CacheBean> cacheList = getCacheList();
        for (CacheBean bean : cacheList) {
            if (bean.playUrl.equals(url)) {
                cacheList.remove(bean);
                break;
            }
        }
        putList(cacheList);

    }

    public static void clear() {
        SharedPreferencesHelper.getInstance().putString(AppSpContact.CACHE_MOVIE, "");
    }


    public static class CacheBean {
        public String playUrl;//真正用于下载的播放地址 同 Chapter.url
        public String name;//电影名字+集数
        public String img;//封面图
        public boolean isSuccess;

        public static CacheBean getBeanFromJsonObject(JSONObject json) {
            CacheBean cacheBean = new CacheBean();
            try {
                cacheBean.playUrl = (String) json.get("playUrl");
                cacheBean.name = (String) json.get("name");
                cacheBean.img = (String) json.get("img");
                cacheBean.isSuccess = (boolean) json.get("isSuccess");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cacheBean;
        }

        public String toJson() {
            return "{" + "\"playUrl\":" + "\"" + playUrl + "\"" + ",\"name\":" + "\"" + name + "\"" + ",\"img\":" + "\"" + img + "\"" + ",\"isSuccess\":" + isSuccess + "}";
        }
    }
}
