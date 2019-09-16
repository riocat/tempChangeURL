package com.yang.tempchangeurl.service;

import com.yang.tempchangeurl.entity.FileConfigBean;
import com.yang.tempchangeurl.entity.TargetURLBean;
import com.yang.tempchangeurl.entity.URLType;
import com.yang.tempchangeurl.http.HttpClientUtil;
import com.yang.tempchangeurl.util.ErrorRecord;
import org.apache.http.HttpEntity;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rio on 2019/9/15.
 */
public class GetURLService {

    public static List<TargetURLBean> getURLList(FileConfigBean fci) {
        BufferedReader br = null;
        InputStreamReader ir = null;
        FileInputStream fi = null;
//        String regexStr = "(.*)HREF=\"(.*)\"\\s(.*)";
        String regexStr = ".*>(.*)<.*";
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = null;
        List<TargetURLBean> result = new ArrayList<TargetURLBean>();
        String errorStr = null;

        try {
            fi = new FileInputStream(fci.getFilePath());
            ir = new InputStreamReader(fi);
            br = new BufferedReader(ir);
            String temp;
            while ((temp = br.readLine()) != null) {
                if (temp.indexOf("HREF") != -1 && temp.indexOf("AVMOO") != -1) {
                    TargetURLBean tb = null;
                    if (temp.indexOf("演员 - 影片") != -1) {
                        matcher = pattern.matcher(temp);
                        if (matcher.find()) {
                            tb = new TargetURLBean();
                            tb.setTempURL(matcher.group(1).replaceAll(" - 演员 - 影片 - AVMOO", ""));
                            tb.setType(URLType.ACTRESS);
                            tb.setOrigionStr(temp);
                        }
                    } else {
                        matcher = pattern.matcher(temp);
                        if (matcher.find()) {
                            errorStr = matcher.group(1);
                            tb = new TargetURLBean();
                            tb.setRealURL(matcher.group(1).substring(0, matcher.group(1).indexOf(" ")));
                            tb.setType(URLType.MOVIE);
                            tb.setOrigionStr(temp);
                        }
                    }

                    result.add(tb);
                } else {
                    ErrorRecord.errorLog(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(errorStr);
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
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fi != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static void GetRealFlie(FileConfigBean fco, List<TargetURLBean> targetURLBeans, String url) {
        PrintWriter pw = null;
        OutputStreamWriter ow = null;
        FileOutputStream fo = null;

        try {
            fo = new FileOutputStream(fco.getFilePath());
            ow = new OutputStreamWriter(fo);
            pw = new PrintWriter(ow);
            for (TargetURLBean turlb : targetURLBeans) {
                if (turlb.getType() == URLType.ACTRESS) {

                    String urlStr = url + URLEncoder.encode(turlb.getTempURL(), "UTF-8");
                    String tempUrl = getMovieRealURL(HttpClientUtil.executeGetRequest(urlStr).getEntity());
                    if (tempUrl != null) {
                        String realUrl = getActressRealURL(HttpClientUtil.executeGetRequest(tempUrl).getEntity(), turlb);

                        if(realUrl != null){
                            StringBuilder sb = new StringBuilder();
                            String origionStr = turlb.getOrigionStr();
                            sb.append(origionStr.substring(0, origionStr.indexOf("\"") + 1));
                            sb.append(realUrl);
                            origionStr = origionStr.substring(origionStr.indexOf("\"") + 1);
                            sb.append(origionStr.substring(origionStr.indexOf("\"")));
                            pw.println(sb.toString());
                        }else{
                            ErrorRecord.errorLog(turlb.getTempURL());
                        }
                    } else {
                        ErrorRecord.errorLog(turlb.getTempURL());
                    }

                } else if (turlb.getType() == URLType.MOVIE) {
                    String urlStr = url + URLEncoder.encode(turlb.getRealURL(), "UTF-8");
                    String realUrl = getMovieRealURL(HttpClientUtil.executeGetRequest(urlStr).getEntity());
                    if (realUrl != null) {
                        StringBuilder sb = new StringBuilder();
                        String origionStr = turlb.getOrigionStr();
                        sb.append(origionStr.substring(0, origionStr.indexOf("\"") + 1));
                        sb.append(realUrl);
                        origionStr = origionStr.substring(origionStr.indexOf("\"") + 1);
                        sb.append(origionStr.substring(origionStr.indexOf("\"")));
                        pw.println(sb.toString());
                    } else {
                        ErrorRecord.errorLog(turlb.getRealURL());
                    }
                }

                Thread.sleep((long) (1000 * (Math.random() * 100 % 5 + 1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (ow != null) {
                try {
                    ow.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getMovieRealURL(HttpEntity httpEntity) {
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

    public static String getActressRealURL(HttpEntity httpEntity, TargetURLBean turlb) {
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
                if (temp.indexOf("<a class=\"avatar-box\"") != -1) {
                    boolean accurateFlag = false;
                    for (int i = 0; i < 4; i++) {
                        if (br.readLine().indexOf(turlb.getTempURL()) != -1) {
                            accurateFlag = true;
                            break;
                        }
                    }
                    if (accurateFlag) {
                        matcher = pattern.matcher(temp);
                        if (matcher.find()) {
                            return matcher.group(2);
                        }
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
}
