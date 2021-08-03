package com.yingda.lkj.beans.pojo.enterprisewechat.approve;

import java.util.List;

public class ApplyDataContent {
    private List<ApplyDataMap> title;
    private ApplyDataValue value;

    public List<ApplyDataMap> getTitle() {
        return title;
    }

    public void setTitle(List<ApplyDataMap> title) {
        this.title = title;
    }

    public ApplyDataValue getValue() {
        return value;
    }

    public void setValue(ApplyDataValue value) {
        this.value = value;
    }
}
