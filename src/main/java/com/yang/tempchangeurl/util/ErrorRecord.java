package com.yang.tempchangeurl.util;

import com.yang.tempchangeurl.entity.TargetURLBean;
import com.yang.tempchangeurl.entity.URLType;
import com.yang.tempchangeurl.http.HttpClientUtil;

import java.io.*;
import java.net.URLEncoder;

/**
 * Created by rio on 2019/9/16.
 */
public class ErrorRecord {

    private static String errorFile = "E:/URLerror.txt";

    {
        new File(errorFile).deleteOnExit();
    }

    public static void errorLog(String urlStr) {
        {
            PrintWriter pw = null;
            OutputStreamWriter ow = null;
            FileOutputStream fo = null;

            try {
                fo = new FileOutputStream(errorFile, true);
                ow = new OutputStreamWriter(fo);
                pw = new PrintWriter(ow);
                pw.println(urlStr);
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
    }
}
