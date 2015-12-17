package com.fenlonsky.ticket.utils;

/**
 * Created by Fenlon on 2015/12/17.
 */
public class UrlUtil {
    public static String fillUrl(String urlTem, String date, String to) {
        String url = urlTem.replace("{date}", date);
        url = url.replace("{to}", to);
        return url;
    }
}
