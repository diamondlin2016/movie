package com.diamond.colamovie.net.down;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 下午5:12
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class M3U8InfoUtils {

    public static M3U8 getM3U8Info(String url) throws IOException {


        BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));


        String basePath = url.substring(0, url.indexOf("/", 10));

        M3U8 ret = new M3U8();
        ret.basePath = basePath;

        String line;
        float seconds = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                if (line.startsWith("#EXTINF:")) {
                    line = line.substring(8);
                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    seconds = Float.parseFloat(line);
                }
                continue;
            }
            if (line.endsWith("m3u8")) {
                return getM3U8Info(basePath + line);
            }
            ret.tsList.add(new M3U8Ts(line, seconds));
            seconds = 0;
        }
        reader.close();

        return ret;

    }
}
