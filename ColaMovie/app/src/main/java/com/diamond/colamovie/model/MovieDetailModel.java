package com.diamond.colamovie.model;

import com.diamond.colamovie.net.UrlApi;
import com.diamond.colamovie.utils.JsoupUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/17 下午5:56
 * Description: 电影页面详情
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/17      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class MovieDetailModel {
    public String img;//缩略图
    public String name;//电影名字
    public ArrayList<TitleKV> titleInfo = new ArrayList<>();//主演 类型 状态 等等~
    public String synopsis;//简介
    public ArrayList<MovieInfoBean> recommend = new ArrayList<>();//猜你喜欢

    public ArrayList<String> playSource = new ArrayList<>();//播放源列表
    public ArrayList<ArrayList<Chapter>> playerList = new ArrayList<>();


    public static MovieDetailModel analysis(Document document) {
        MovieDetailModel bean = new MovieDetailModel();
        bean.img = UrlApi.HOME + document.getElementsByClass("ct-l").get(0).getElementsByClass("lazy").get(0).attr("data-original");
        bean.name = document.getElementsByClass("ct-c").get(0).getElementsByClass("name").get(0).ownText();
        bean.titleInfo.addAll(analysisTitleInfo(document, "ct-c"));
        bean.synopsis = document.getElementsByClass("ct-c").get(0).getElementsByClass("ee").get(0).text();
        bean.recommend.addAll(analysisClass(document, "link-hover"));
        bean.playSource = analysisPlayerSource(document, "playfrom tab8 clearfix");
        bean.playerList = analysisPlayerList(document, "stab1");


        return bean;
    }

    private static ArrayList<ArrayList<Chapter>> analysisPlayerList(Document document, String id) {
        ArrayList<ArrayList<Chapter>> lists = new ArrayList<>();
        Element element = document.getElementById(id);
        if (element != null) {
            Elements divs = element.children();
            for (int i = 1; i < divs.size(); i++) {
                Element element1 = divs.get(i);//stab81 stab82
                Elements div = element1.children();
                for (Element e : div) {
                    if (!"h1 clearfix".equals(e.className())) {
                        Elements all = e.getElementsByTag("a");
                        ArrayList<Chapter> chapters = new ArrayList<>();
                        for (Element a : all) {
                            Chapter chapter = new Chapter();
                            chapter.name = a.attr("title");
                            chapter.url = UrlApi.HOME + a.attr("href");
                            chapters.add(chapter);
                        }
                        lists.add(chapters);
                    }
                }

            }
        }

        return lists;
    }

    private static ArrayList<String> analysisPlayerSource(Document document, String clazz) {
        ArrayList<String> list = new ArrayList<>();
        Element ul = document.getElementsByClass(clazz).first();
        if (ul != null) {
            Elements lis = ul.getElementsByTag("li");
            for (Element li : lis) {
                list.add(li.ownText());
            }
        }
        return list;
    }

    //获取 title
    private static Collection<? extends TitleKV> analysisTitleInfo(Document document, String clazz) {
        ArrayList<TitleKV> list = new ArrayList<>();
        Elements elementsByTag = document.getElementsByClass(clazz);
        Elements dl = elementsByTag.get(0).getElementsByTag("dl").get(0).children();
        for (int i = 1; i < dl.size(); i++) {//不要第一组
            TitleKV titleKV = new TitleKV();
            Element dtdd = dl.get(i);
            titleKV.key = dtdd.getElementsByTag("span").get(0).text();//主演 类型 状态等
            Elements a = dtdd.getElementsByTag("a");
            if (a.size() == 0) {
                titleKV.value = dtdd.ownText();
            } else {
                StringBuffer value = new StringBuffer();
                for (Element a1 : a) {
                    String str = a1.ownText();
                    value.append(str);
                }
                titleKV.value = value.toString();
            }
            list.add(titleKV);
        }

        return list;
    }

    private static ArrayList<? extends MovieInfoBean> analysisClass(Document document, String tag) {


        return JsoupUtils.getMovieCard(document.getElementsByClass(tag));
    }


    public static class TitleKV {
        public String key;
        public String value;
    }

    public static class Chapter {

        public String name;//章节名
        public String url;//url

        public Chapter(String name, String url) {
            this.name = name;
            this.url = url;
        }
        public Chapter() {
        }
    }

}
