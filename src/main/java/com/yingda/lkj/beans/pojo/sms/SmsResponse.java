package com.yingda.lkj.beans.pojo.sms;

import java.util.List;

public class SmsResponse {
    private List<SmsResult> result;
    private String code;
    private String description;

    public List<SmsResult> getResult() {
        return result;
    }

    public void setResult(List<SmsResult> result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
