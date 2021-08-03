package com.yingda.lkj.beans.pojo.enterprisewechat.approve;

import java.util.List;

public class ApplyDataValue {
    private String text;
    private ApplyDataSelector selector;
    private ApplyDataDate date;
    private String new_money;
    private List<ApplyDataValueList> children;
    private List<ApplayDataMember> members;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ApplyDataSelector getSelector() {
        return selector;
    }

    public void setSelector(ApplyDataSelector selector) {
        this.selector = selector;
    }

    public ApplyDataDate getDate() {
        return date;
    }

    public void setDate(ApplyDataDate date) {
        this.date = date;
    }

    public String getNew_money() {
        return new_money;
    }

    public void setNew_money(String new_money) {
        this.new_money = new_money;
    }

    public List<ApplyDataValueList> getChildren() {
        return children;
    }

    public void setChildren(List<ApplyDataValueList> children) {
        this.children = children;
    }

    public List<ApplayDataMember> getMembers() {
        return members;
    }

    public void setMembers(List<ApplayDataMember> members) {
        this.members = members;
    }
}
