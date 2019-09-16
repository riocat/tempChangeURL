package com.yang.tempchangeurl.entity;

/**
 * Created by rio on 2019/9/14.
 */
public class TargetURLBean {

    private String realURL;

    private String tempURL;

    private URLType type;

    private String origionStr;

    public String getRealURL() {
        return realURL;
    }

    public void setRealURL(String realURL) {
        this.realURL = realURL;
    }

    public String getTempURL() {
        return tempURL;
    }

    public void setTempURL(String tempURL) {
        this.tempURL = tempURL;
    }

    public URLType getType() {
        return type;
    }

    public void setType(URLType type) {
        this.type = type;
    }

    public String getOrigionStr() {
        return origionStr;
    }

    public void setOrigionStr(String origionStr) {
        this.origionStr = origionStr;
    }
}
