package com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.value;

import com.yingda.lkj.beans.entity.system.User;

/**
 * 成员控件
 */
public class ApproveValueMemberUser {
    private String userid;
    private String name;

    public ApproveValueMemberUser() {
    }

    public ApproveValueMemberUser(User user) {
        this.userid = user.getId();
        this.name = user.getDisplayName();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
