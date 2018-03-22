package com.diamond.colamovie.net.down;


import java.util.ArrayList;
import java.util.List;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 下午5:17
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class M3U8 {

    public String basePath;//去除后缀文件名的url
    public String m3u8FilePath;//m3u8索引文件路径
    public String dirFilePath;//切片文件目录
    public long fileSize;//切片文件总大小
    public long totalTime;//总时间，单位毫秒
    public List<M3U8Ts> tsList = new ArrayList<>();//视频切片

}
