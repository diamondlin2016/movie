package com.diamond.colamovie.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/21 下午4:50
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/21      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class AnalyzeUrlModel {

    public static String analysisToken(Document document) {
        String str = "";
        Elements script = document.getElementsByTag("script");
        for (Element s : script) {
            String text = s.data();
            if (text.contains("requestToken")) {
                String[] split = text.split("requestToken");
                if (split.length > 1) {
                    String s1 = split[1].trim();
                    int star = s1.indexOf("\"") + 1;
                    int end = s1.indexOf("\"", star);
                    if (end > star)
                        str = s1.substring(star, end);
                }
                break;
            }
        }
        return str;
    }


    public static String analysisPlay(Document document) {
        String play = "";
        String text = document.text();
        return play;
    }
}
