package com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.value;

public class ApproveValueMoney {
    private String new_money;

    public ApproveValueMoney() {
    }

    public ApproveValueMoney(Double money) {
        this.new_money = money.toString();
    }

    public String getNew_money() {
        return new_money;
    }

    public void setNew_money(String new_money) {
        this.new_money = new_money;
    }
}
