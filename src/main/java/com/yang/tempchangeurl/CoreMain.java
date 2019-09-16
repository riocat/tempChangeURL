package com.yang.tempchangeurl;

import com.yang.tempchangeurl.entity.FileConfigBean;
import com.yang.tempchangeurl.entity.TargetURLBean;
import com.yang.tempchangeurl.service.GetURLService;

import java.util.List;

/**
 * Created by rio on 2019/9/14.
 */
public class CoreMain {

    public static void main(String[] args) {
        FileConfigBean fci = new FileConfigBean();
        fci.setFilePath("E:/bookmarks_2019_9_1 - 副本 - 副本.html");
        FileConfigBean fco = new FileConfigBean();
        fco.setFilePath("E:/target.txt");
        String url = "https://avmoo.asia/cn/search/";

        List<TargetURLBean> targetURLBeans =  GetURLService.getURLList(fci);

//        System.out.println(targetURLBeans.size());

        GetURLService.GetRealFlie(fco, targetURLBeans, url);
    }
}
