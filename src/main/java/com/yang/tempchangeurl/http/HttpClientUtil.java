package com.yang.tempchangeurl.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rio on 2019/9/14.
 */
public class HttpClientUtil {

    public static CloseableHttpClient closeableHttpClient = null;

    public static CloseableHttpClient getHttpClient(HashMap configMap) {
/*        if (closeableHttpClient == null) {
            if (configMap == null || configMap.size() < 1) {
                closeableHttpClient = HttpClients.createDefault();
            } else {
                closeableHttpClient = HttpClients.custom().build();
            }
        }
        return closeableHttpClient;*/

        if (configMap == null || configMap.size() < 1) {
            return HttpClients.createDefault();
        } else {
            return HttpClients.custom().build();
        }
    }

    public static HttpResponse executeGetRequest(String url) {
        try {
            CloseableHttpClient closeableHttpClient = getHttpClient(null);
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            return httpResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void testClient() {
        try {
            String url1 = "https://avmoo.asia/cn/search/";
            CloseableHttpClient closeableHttpClient = getHttpClient(null);
            url1 = url1 + URLEncoder.encode("SOUL-051 凌辱女校長 大野実花 ", "UTF-8");
            HttpGet httpGet = new HttpGet(url1);
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            System.out.println(getRealURL(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRealURL(HttpEntity httpEntity) {
        BufferedReader br = null;
        InputStreamReader ir = null;
        // "<a class=\"movie-box\" href=\"https://avmoo.asia/cn/movie/657b88a2decb8916\">"
        String regexStr = "(.*)=\"(.*)\">";
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = null;

        try {
            ir = new InputStreamReader(httpEntity.getContent(), "UTF-8");
            br = new BufferedReader(ir);
            String temp;
            while ((temp = br.readLine()) != null) {
                if (temp.indexOf("<a class=\"movie-box\"") != -1) {
                    matcher = pattern.matcher(temp);
                    if (matcher.find()) {
                        return matcher.group(2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (ir != null) {
                try {
                    ir.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {

        testClient();
//
//        StringBuilder sb = new StringBuilder();
//        String origionStr = "            <DT><A HREF=\"https://avmoo.asia/cn/movie/6jro\" ADD_DATE=\"1524438818\">TITG-013 乳妻 静香 - AVMOO</A>";
//        sb.append(origionStr.substring(0,origionStr.indexOf("\"") + 1));
//        sb.append("https://avmoo.asia/cn/movie/4ebeb2e9cb00519d");
//        origionStr = origionStr.substring(origionStr.indexOf("\"") + 1);
//        sb.append(origionStr.substring(origionStr.indexOf("\"")));
//        System.out.println(sb.toString());
    }

}
