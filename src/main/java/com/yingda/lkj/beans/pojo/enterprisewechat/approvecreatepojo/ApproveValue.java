
package com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo;

import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.pojo.enterprisewechat.approve.ApproveRewardDetail;
import com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.value.ApproveValueDate;
import com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.value.ApproveValueMemberUser;
import com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.value.ApproveValueTable;
import com.yingda.lkj.utils.StreamUtil;

import java.sql.Timestamp;
import java.util.List;

/**
 * 控件值
 */
public class ApproveValue {

    private String text;
    private ApproveValueDate date;
    private List<ApproveValueTable> children;
    private List<ApproveValueMemberUser> members;
    private String new_money;

    public ApproveValue() {
    }

    public ApproveValue(String textOrMoney) {
        this.text = textOrMoney;
    }

    public ApproveValue(Timestamp timestamp) {
        this.date = new ApproveValueDate(timestamp);
    }

    public ApproveValue(ApproveRewardDetail approveRewardDetail) {
        List<User> users = approveRewardDetail.getUsers();
        this.members = StreamUtil.getList(users, ApproveValueMemberUser::new);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ApproveValueDate getDate() {
        return date;
    }

    public void setDate(ApproveValueDate date) {
        this.date = date;
    }

    public List<ApproveValueTable> getChildren() {
        return children;
    }

    public void setChildren(List<ApproveValueTable> children) {
        this.children = children;
    }

    public List<ApproveValueMemberUser> getMembers() {
        return members;
    }

    public void setMembers(List<ApproveValueMemberUser> members) {
        this.members = members;
    }

    public String getNew_money() {
        return new_money;
    }

    public void setNew_money(String new_money) {
        this.new_money = new_money;
    }
}
