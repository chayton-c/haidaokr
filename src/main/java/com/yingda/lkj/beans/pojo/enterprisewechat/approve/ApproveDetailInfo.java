package com.yingda.lkj.beans.pojo.enterprisewechat.approve;

public class ApproveDetailInfo {
    private String sp_no;
    private String sp_name;
    private String sp_status;
    private ApproveDetailApplyer applyer;
    private ApproveApplyData apply_data;

    public ApproveDetailApplyer getApplyer() {
        return applyer;
    }

    public void setApplyer(ApproveDetailApplyer applyer) {
        this.applyer = applyer;
    }

    public ApproveApplyData getApply_data() {
        return apply_data;
    }

    public void setApply_data(ApproveApplyData apply_data) {
        this.apply_data = apply_data;
    }

    public String getSp_no() {
        return sp_no;
    }

    public void setSp_no(String sp_no) {
        this.sp_no = sp_no;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public String getSp_status() {
        return sp_status;
    }

    public void setSp_status(String sp_status) {
        this.sp_status = sp_status;
    }
}
