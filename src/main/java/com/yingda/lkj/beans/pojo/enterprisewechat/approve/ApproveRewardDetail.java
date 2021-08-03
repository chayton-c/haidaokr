package com.yingda.lkj.beans.pojo.enterprisewechat.approve;

import com.yingda.lkj.beans.entity.system.User;

import java.util.List;

public class ApproveRewardDetail {
    private List<User> users;
    private Double amount;

    public ApproveRewardDetail() {
    }

    public ApproveRewardDetail(List<User> users, Double amount) {
        this.users = users;
        this.amount = amount;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
