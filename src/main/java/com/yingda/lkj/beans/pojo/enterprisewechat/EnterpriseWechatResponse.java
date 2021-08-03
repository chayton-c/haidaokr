package com.yingda.lkj.beans.pojo.enterprisewechat;

import com.yingda.lkj.beans.pojo.enterprisewechat.department.EnterpriseWechatDepartmentResponse;
import com.yingda.lkj.beans.pojo.enterprisewechat.user.WeChatUserResponse;

import java.util.List;

public class EnterpriseWechatResponse {

    private Integer errcode;
    private String errmsg;
    private List<EnterpriseWechatDepartmentResponse> department;
    private List<WeChatUserResponse> userlist;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<EnterpriseWechatDepartmentResponse> getDepartment() {
        return department;
    }

    public void setDepartment(List<EnterpriseWechatDepartmentResponse> department) {
        this.department = department;
    }

    public List<WeChatUserResponse> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<WeChatUserResponse> userlist) {
        this.userlist = userlist;
    }
}
