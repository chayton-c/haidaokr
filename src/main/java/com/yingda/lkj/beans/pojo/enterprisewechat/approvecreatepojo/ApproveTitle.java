package com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo;

public class ApproveTitle {
    private String text;
    private String lang = "zh_CN";

    public ApproveTitle() {
    }

    public ApproveTitle(String text) {
        this.text = text;
        this.lang = "zh_CN";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
