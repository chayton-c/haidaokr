package com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.value;

import java.sql.Timestamp;

public class ApproveValueDate {
    private String type = "day";
    private String s_timestamp;

    public ApproveValueDate() {
    }

    public ApproveValueDate(Timestamp timestamp) {
        this.type = "day";
        this.s_timestamp = timestamp.getTime() / 1000 + "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getS_timestamp() {
        return s_timestamp;
    }

    public void setS_timestamp(String s_timestamp) {
        this.s_timestamp = s_timestamp;
    }
}
