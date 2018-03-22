package com.diamond.colamovie.model;

import android.text.TextUtils;

import com.diamond.colamovie.utils.EscpUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chuangyuan.ycj.videolibrary.listener.ItemVideo;


/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/18 下午2:05
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/18      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class MoviePlayUrlModel {

    public ArrayList<MovieDetailModel.Chapter> playerList = new ArrayList<>();
    public boolean hasData = false;


    private void checkData() {
        hasData = playerList.size() != 0;
    }


    //-------------------------新解析规则------------------

    public static MoviePlayUrlModel analysis(Document document) {
        MoviePlayUrlModel moviePlayUrlModel = new MoviePlayUrlModel();
        Elements scripts = document.getElementsByTag("script");
        for (Element script : scripts) {
            if (script.data().contains("mac_url")) {
                String data = script.data();
//                            data = data.split("unescape(\'")[1];
                int end = data.lastIndexOf("')");
                int start = data.indexOf("unescape(") + 10;
                String u = data.substring(start, end);
                u = EscpUtils.unescape(u);

                moviePlayUrlModel.playerList = getUrlFromString(u);
                break;
            }
        }
        moviePlayUrlModel.checkData();

        return moviePlayUrlModel;
    }

    private static ArrayList<MovieDetailModel.Chapter> getUrlFromString(String str) {
        ArrayList<MovieDetailModel.Chapter> list = new ArrayList<>();
        if (!str.contains("第02集")) {
            //电影
            String[] strings = str.split("\\$\\$\\$");
            for (String s : strings) {
                String[] split = s.split("\\$");
                list.add(new MovieDetailModel.Chapter(split[0], split[1]));
            }

        } else {
            //电视剧 用 # 切割
            String[] splits = str.split("#");
            for (String s : splits) {

                String[] split = s.split("\\$");
                for (int i = 1; i <= split.length; i += 2) {
                    if (!TextUtils.isEmpty(split[i]))
                        list.add(new MovieDetailModel.Chapter(split[i - 1], split[i]));
                }

            }
        }


        return list;

    }

    public static int getIntegerFromString(String string) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return Integer.valueOf(m.replaceAll("").trim());
    }


    public class MyItemVideo implements ItemVideo {

        private final String mUri;

        public MyItemVideo(String uri) {
            mUri = uri;
        }

        @Override
        public String getVideoUri() {
            return mUri;
        }
    }
}
